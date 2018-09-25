package de.uniol.inf.is.odysseus.sweeparea;

import java.util.List;

/**
 * 
 * @author Marco Grawunder
 **/

public interface IFastList<E> extends List<E> {

	public void removeRange(int fromIndex, int toIndex);
	
}