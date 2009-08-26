package de.uniol.inf.is.odysseus.base;

/**
 * First Element Sorted Pair
 * @author Marco Grawunder
 *
 * @param <E1>
 * @param <E2>
 */
public class FESortedPair<E1 extends Comparable<E1>,E2> extends UnsortedPair<E1, E2> implements Comparable<FESortedPair<E1,E2>>{
		
	public FESortedPair(E1 e1, E2 e2) {
		super(e1, e2);
	}

	@Override
	public int compareTo(FESortedPair<E1, E2> o) {
		return this.getE1().compareTo(o.getE1());
	}
}