package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class BiddingProviderExecutionHandler<T extends AbstractPeer> extends AbstractExecutionHandler<T>{

	private AbstractDistributionProvider<AbstractPeer> provider;

	public BiddingProviderExecutionHandler(Lifecycle lifecycle, AbstractDistributionProvider<AbstractPeer> provider, T peer) {
		super(lifecycle);
		this.provider = provider;
		setPeer(peer);
	}

	@Override
	public void run() {
		if(getExecutionListenerCallback() != null) {
			getProvider().distributePlan(getExecutionListenerCallback().getQuery(), getPeer().getServerResponseAddress());
		}
	}
	
	public AbstractDistributionProvider<AbstractPeer> getProvider() {
		return provider;
	}
	
}
