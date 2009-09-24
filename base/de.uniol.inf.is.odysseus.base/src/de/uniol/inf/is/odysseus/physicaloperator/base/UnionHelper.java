package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.PointInTime;

public interface UnionHelper<T> {
	
	public PointInTime getStart(T elem);
	public T getReferenceElement(PointInTime minTs, T elem);
}
