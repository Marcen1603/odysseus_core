package de.uniol.inf.is.odysseus.core.objecthandler;

import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;

abstract public class AbstractByteBufferHandler<R,W> implements IInputDataHandler<R,W> {

	@Override
    public abstract IInputDataHandler<R,W> clone();
	
}
