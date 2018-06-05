package de.uniol.inf.is.odysseus.sweeparea;

import java.util.ArrayList;

public class FastArrayList<E> extends ArrayList<E> implements IFastList<E> {

	private static final long serialVersionUID = -7748807868601751346L;

	@Override
	public void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}
	
}
