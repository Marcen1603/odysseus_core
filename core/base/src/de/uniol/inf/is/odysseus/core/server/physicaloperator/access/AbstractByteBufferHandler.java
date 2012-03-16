package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

abstract public class AbstractByteBufferHandler<R,W> implements IInputDataHandler<R,W> {

	public abstract IInputDataHandler<R,W> clone();
	
}
