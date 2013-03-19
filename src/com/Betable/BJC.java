package com.Betable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Blackjack face values.
 * @author yun-chiao
 *
 */
enum C {
	A(1, 11), _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8), _9(9), _10(10), J(
			10), Q(10), K(10);
	private int[] faceValues;

	C(int... faceValues) {
		this.faceValues = faceValues;
	}

	int[] faceValues() {
		return Arrays.copyOf(faceValues, faceValues.length);
	}
}

/**
 * Card suites.
 * @author yun-chiao
 *
 */
enum S {
	Club, Diamond, Heart, Spade;
}

/**
 * Suite + face values.
 * @author yun-chiao
 *
 */
class BJC {
	final S s;
	final C c;

	private BJC(S s, C c) {
		this.s = s;
		this.c = c;
	}

	@Override
	public String toString() {
		return String.format("%s %s", s, c);
	}

	static List<BJC> getDeck() {
		List<BJC> deck = new ArrayList<BJC>();
		for (S s : S.values())
			for (C c : C.values())
				deck.add(new BJC(s, c));
		return deck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
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
		BJC other = (BJC) obj;
		if (c != other.c)
			return false;
		if (s != other.s)
			return false;
		return true;
	}

}