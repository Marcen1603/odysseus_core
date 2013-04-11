package de.uniol.inf.is.odysseus.core.server.physicaloperator.sa;

import java.util.List;

public interface IFastList<E> extends List<E> {

	public void removeRange(int fromIndex, int toIndex);
	
}
