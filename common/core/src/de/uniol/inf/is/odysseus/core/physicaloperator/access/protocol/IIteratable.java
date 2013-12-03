package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

public interface IIteratable<T> {
	
	boolean hasNext();
	T getNext();

}
