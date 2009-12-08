package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.IDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

@SuppressWarnings("unchecked")
public class BiddingProviderExecutionHandler<P extends AbstractPeer,F extends IDistributionProvider> extends AbstractExecutionHandler<P, F> {

	
	public BiddingProviderExecutionHandler(Lifecycle lifecycle,
			F function, P peer) {
		super(lifecycle, function, peer);
	}


	@Override
	public String getName() {
		return "BiddingProvider";
	}

	@Override
	public void run() {
		if(getExecutionListenerCallback() != null) {
			getFunction().distributePlan(getExecutionListenerCallback(), getPeer().getServerResponseAddress());
		}
	}

	
	
}
