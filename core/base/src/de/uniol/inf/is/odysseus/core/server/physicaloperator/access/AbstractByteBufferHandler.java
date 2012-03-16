package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

abstract public class AbstractByteBufferHandler<T> implements IByteBufferHandler<T> {

	public abstract IByteBufferHandler<T> clone();
	
}
