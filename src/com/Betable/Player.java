package com.Betable;

/**
 * (table position, player id) pair
 * 
 * @author yun-chiao
 * 
 */
class PlayerTuple {
	public final int pos;
	public final long id;

	PlayerTuple(int pos, long id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("(pos=%d, pid=%d)", pos, id);
	}
}

/**
 * A player of Blackjack
 * 
 * @author yun-chiao
 * 
 */
class Player {
	public final int pos;
	public final long pid;
	final BJHand hand = new BJHand();

	Player(int pos, long id) {
		this.pid = id;
		this.pos = pos;
	}

	boolean hit() {
		if (hand.busted())
			return false;
		return hand.score() < 17;
	}

	@Override
	public String toString() {
		return String.format("%d:%s", pos, hand);
	}
}