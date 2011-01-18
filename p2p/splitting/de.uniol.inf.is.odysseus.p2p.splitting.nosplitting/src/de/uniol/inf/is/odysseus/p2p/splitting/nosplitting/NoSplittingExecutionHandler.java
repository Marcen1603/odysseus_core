package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;

public class NoSplittingExecutionHandler extends AbstractSplittingExecutionHandler<AbstractSplittingStrategy>{

	public NoSplittingExecutionHandler(
			NoSplittingExecutionHandler noSplittingExecutionHandler) {
		super(noSplittingExecutionHandler);
	}
	
	public NoSplittingExecutionHandler() {
		super();
	}
	
	@Override
	public IExecutionHandler<AbstractSplittingStrategy> clone()  {
		return new NoSplittingExecutionHandler(this);
	}

	@Override
	public String getName() {
		return "NoSplittingExecutionHandler";
	}
	
}
