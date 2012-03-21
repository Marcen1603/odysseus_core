package de.uniol.inf.is.odysseus.core.server.objecthandler;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IInputDataHandler;

abstract public class AbstractByteBufferHandler<R,W> implements IInputDataHandler<R,W> {

	@Override
    public abstract IInputDataHandler<R,W> clone();
	
}
