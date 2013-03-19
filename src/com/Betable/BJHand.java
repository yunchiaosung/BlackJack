package com.Betable;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Cards dealt to a player.
 * @author yun-chiao
 *
 */
class BJHand implements Comparable<BJHand> {
	Set<BJC> cards = new LinkedHashSet<BJC>();

	int score() {
		Set<Integer> scores = possibleScores();
		int maxUnder = Integer.MIN_VALUE;
		for (int score : scores) {
			if (score <= 21 && score > maxUnder) {
				maxUnder = score;
			}
		}
		return maxUnder;
	}

	private Set<Integer> possibleScores() {
		Set<Integer> scores = new HashSet<Integer>(Collections.singleton(0));
		for (BJC card : cards) {
			scores = addCardToScoreList(card, scores);
		}
		return scores;
	}

	private Set<Integer> addCardToScoreList(BJC card, Set<Integer> scores) {
		Set<Integer> newScores = new HashSet<Integer>();
		for (int score : scores) {
			for (int v : card.c.faceValues())
				newScores.add(score + v);
		}
		return newScores;
	}

	boolean addCard(BJC card) {
		return cards.add(card);
	}

	boolean busted() {
		int sc = score();
		return sc < 0 || sc > 21;
	}

	boolean is21() {
		return score() == 21;
	}

	boolean isBlackJack() {
		if (cards.size() != 2)
			return false;
		if (score() != 21)
			return false;

		BJC[] _cards = cards.toArray(new BJC[2]);
		C first = _cards[0].c;
		C second = _cards[1].c;
		return first == C.A || second == C.A;
	}

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int compareTo(BJHand that) {
		if (isBlackJack())
			return that.isBlackJack() ? 0 : 1;
		if (that.isBlackJack())
			return -1;
		return new Integer(score()).compareTo(new Integer(that.score()));
	}
}