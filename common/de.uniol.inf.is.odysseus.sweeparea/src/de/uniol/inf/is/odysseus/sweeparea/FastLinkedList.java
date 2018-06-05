package de.uniol.inf.is.odysseus.sweeparea;

import java.util.LinkedList;

public class FastLinkedList<E> extends LinkedList<E> implements IFastList<E> {
	
	private static final long serialVersionUID = 3021304062680824940L;
	
	@Override
	public void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}

}
