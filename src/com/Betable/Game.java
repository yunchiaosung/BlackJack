package com.Betable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Shuffle a deck of cards.
 * @author yun-chiao
 *
 */
class Shuffler {
	private static Random r = new Random(0x0823);

	private Shuffler() {;}

	static <T> void shuffle(List<T> ls) {
		int size = ls.size();
		for (int i = 0; i < size; i++) {
			int j = i + r.nextInt(size - i);
			T t1 = ls.get(i);
			T t2 = ls.get(j);
			ls.set(i, t2);
			ls.set(j, t1);
		}
	}
}

/**
 * Player decision.
 * @author yun-chiao
 *
 */
enum Action {
	HIT, STAND
}

/**
 * State for a game of Blackjack.
 * @author yun-chiao
 *
 */
class Game {
	private static final int MAX_PLAYERS = 5;
	public final long gid;
	private List<BJC> deck = BJC.getDeck();
	private final Map<Long, Player> players = new LinkedHashMap<Long, Player>();
	private Iterator<Player> iter;

	volatile private Player cur = null;
	volatile private long ts;

	Game(long id) {
		this.gid = id;
		Shuffler.<BJC> shuffle(deck);
	}

	public long getGid(){
		return gid;
	}
	
	long getTs() {
		return ts;
	}

	Player getCur() {
		return cur;
	}

	int size() {
		return players.size();
	}

	boolean started() {
		return iter != null;
	}

	boolean ended() {
		return iter != null && cur == null;
	}

	boolean addPlayer(Player p) {
		if (players.size() >= MAX_PLAYERS)
			return false;
		return players.put(p.pid, p) == null;
	}

	boolean hasPlayer(long pid) {
		return players.containsKey(pid);
	}

	void deal(List<BJC> deck, Player p) {
		int last = deck.size() - 1;
		BJC c = deck.remove(last);
		p.hand.addCard(c);
	}

	boolean start() {
		if (started())
			return false;
		if (players.isEmpty())
			return false;

		for (int i = 0; i < 2; i++)
			for (Player p : players.values())
				deal(deck, p);

		ts=System.currentTimeMillis();
		iter = players.values().iterator();
		cur = iter.next();
		return true;
	}

	boolean act(long pid, Action a) {
		if (!started())
			return false;
		if (cur.pid != pid)
			return false;
		if (a == Action.HIT) {
			deal(deck, cur);
			ts = System.currentTimeMillis();
		}
		if (cur.hand.busted() || a == Action.STAND)
			next();
		return true;
	}

	boolean next() {
		ts = System.currentTimeMillis();
		boolean hasNext = iter.hasNext();
		cur = hasNext ? iter.next() : null;
		return hasNext;
	}

	Set<Player> winners() {
		if (!ended())
			return Collections.emptySet();

		Set<Player> w = new HashSet<Player>();
		int max = Integer.MIN_VALUE;
		for (Player p : players.values()) {
			int sc = p.hand.score();
			if (sc == max)
				w.add(p);
			else if (sc > max) {
				max = sc;
				w.clear();
				w.add(p);
			}
		}
		return max == Integer.MIN_VALUE ? Collections.<Player> emptySet() : w;
	};

	void play() {
		List<BJC> deck = BJC.getDeck();
		Shuffler.<BJC> shuffle(deck);

		System.out.println("initial hands ...");
		for (int i = 0; i < 2; i++)
			for (Player p : players.values())
				deal(deck, p);
		System.out.println(this);

		for (Player p : players.values()) {
			System.out.println("dealing player " + p.pid);
			while (p.hit()) {
				deal(deck, p);
				System.out.println(this);
			}
		}

		for (Player p : players.values())
			System.out.println("player " + p.pid + " " + p.hand.score());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gid ^ (gid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (gid != other.gid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format(" --- game %d @ %s --- \n", gid, cur));
		for (Player p : players.values())
			sb.append(p).append('\n');
		sb.append(" ---\n");
		return sb.toString();
	}
}