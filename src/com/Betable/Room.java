package com.Betable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Room full of Blackjack games.
 * 
 * @author yun-chiao
 * 
 */
class Room {
	private static Random r = new Random(1985);
	Map<Long, Game> bjGames = new HashMap<Long, Game>();

	Game addGame() {
		long gid;
		Game g;
		while (bjGames.containsKey(gid = r.nextLong()))
			/* generate next game id */;
		bjGames.put(gid, g = new Game(gid));
		return g;
	}

	PlayerTuple joinGame(long gid) {
		Game g = bjGames.get(gid);
		if (g == null)
			return null;
		if (g.started())
			return null;

		int pos;
		long pid;
		while (g.hasPlayer(pid = r.nextLong()))
			/* generate next pid */;
		return g.addPlayer(new Player(pos = g.size(), pid)) ? new PlayerTuple(
				pos, pid) : null;
	}

	Collection<Game> listGames() {
		return bjGames.values();
	}

	Game getGame(long gid) {
		return bjGames.get(gid);
	}

	Game startGame(long gid) {
		Game g = bjGames.get(gid);
		if (g == null)
			return null;
		return g.start() ? g : null;
	}

	Game act(long gid, long pid, Action a) {
		Game g = bjGames.get(gid);
		if (g == null)
			return null;

		return g.act(pid, a) ? g : null;
	}

	Collection<Player> winners(long gid) {
		Game g = bjGames.get(gid);
		if (g == null)
			return Collections.emptySet();
		return g.winners();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String line;
		Room room = new Room();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((line = br.readLine()) != null) {
			long gid, pid;
			String[] opts = line.split(" ");
			switch (opts[0].charAt(0)) {
			case 'a':
				System.out.println(room.addGame());
				break;
			case 'j':
				gid = Long.parseLong(opts[1]);
				System.out.println(room.joinGame(gid));
				break;
			case 'l':
				for (Game g : room.listGames())
					System.out.println(g.gid);
				break;
			case 'g':
				gid = Long.parseLong(opts[1]);
				System.out.println(room.getGame(gid));
				break;
			case 's':
				gid = Long.parseLong(opts[1]);
				System.out.println(room.startGame(gid));
				break;
			case 'h':
				gid = Long.parseLong(opts[1]);
				pid = Long.parseLong(opts[2]);
				System.out.println(room.act(gid, pid, Action.HIT));
				break;
			case 't':
				gid = Long.parseLong(opts[1]);
				pid = Long.parseLong(opts[2]);
				System.out.println(room.act(gid, pid, Action.STAND));
				break;
			case 'w':
				gid = Long.parseLong(opts[1]);
				for (Player p : room.winners(gid))
					System.out.println(p.pid);
				break;
			}
			System.out.println("===");
		}
	}
}