package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.execution.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractDistributionExecutionHandler extends AbstractExecutionHandler{

	
	
	public AbstractDistributionExecutionHandler(IDistributionProvider distributionProvider) {
		super(Lifecycle.DISTRIBUTION);
		this.distributionProvider = distributionProvider;
	}

	private IDistributionProvider distributionProvider;


	@Override
	public abstract void run();

}
