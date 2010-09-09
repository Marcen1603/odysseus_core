package de.uniol.inf.is.odysseus.p2p.splitting.base;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractSplittingExecutionHandler extends AbstractExecutionHandler<AbstractPeer, ISplittingStrategy> {


	public AbstractSplittingExecutionHandler(Lifecycle lifecycle,
			ISplittingStrategy function, AbstractPeer peer) {
		super(lifecycle, function, peer);
	}

	private ISplittingStrategy splittingStrategy;


	@Override
	public void run() {
	}

	@Override
	public String getName() {
		return null;
	}

	public ISplittingStrategy getSplittingStrategy() {
		return splittingStrategy;
	}

}
