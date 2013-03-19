package com.Betable;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Game administrator; this class deals cards, manages time, responds to player
 * requests.
 * 
 * @author yun-chiao
 * 
 */
class GameAdmin {
	private final Room room;
	private final ExecutorService worker = Executors.newSingleThreadExecutor();
	private final ExecutorService timer = Executors.newSingleThreadExecutor();

	GameAdmin(final Room room) {
		this.room = room;
		timer.submit(new Runnable() {
			@Override
			public void run() {
				for (;;) {
					long curTs = System.currentTimeMillis();
					for (Game g : room.listGames()) {
						Player cur = g.getCur();
						if (cur != null
								&& curTs - g.getTs() > TimeUnit.SECONDS
										.toMillis(30))
							serve(new BJRequest(Operation.Stand, g.gid, cur.pid));
					}
					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(1));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		});

	}

	Object serve(final BJRequest req) {
		Future<?> future = null;

		switch (req.op) {
		case Help:
			return Operation.help();
		case Add:
		case Get:
		case Start:
			future = worker.submit(new Callable<Game>() {
				@Override
				public Game call() {
					return req.op == Operation.Add ? room.addGame()
							: req.op == Operation.Get ? room.getGame(req.gid)
									: room.startGame(req.gid);
				}
			});
			break;

		case Join:
			future = worker.submit(new Callable<PlayerTuple>() {
				@Override
				public PlayerTuple call() throws Exception {
					return room.joinGame(req.gid);
				}
			});
			break;
		case List:
			future = worker.submit(new Callable<Collection<Game>>() {
				@Override
				public Collection<Game> call() throws Exception {
					return room.listGames();
				}
			});
			break;

		case Hit:
		case Stand:
			future = worker.submit(new Callable<Game>() {
				@Override
				public Game call() {
					return room
							.act(req.gid, req.pid,
									req.op == Operation.Hit ? Action.HIT
											: Action.STAND);
				}
			});
			break;
		case Win:
			future = worker.submit(new Callable<Collection<Player>>() {
				@Override
				public Collection<Player> call() {
					return room.winners(req.gid);
				}
			});
			break;
		default:
			break;
		}

		try {
			return future.get(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			future.cancel(false);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
		} catch (TimeoutException e) {
		}
		return null;
	}
}
