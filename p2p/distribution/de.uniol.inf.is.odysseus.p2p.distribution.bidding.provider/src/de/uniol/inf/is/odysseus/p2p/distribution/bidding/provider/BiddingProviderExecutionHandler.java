package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

@SuppressWarnings("unchecked")
public class BiddingProviderExecutionHandler extends AbstractExecutionHandler<AbstractPeer, AbstractDistributionProvider> {

	


	public BiddingProviderExecutionHandler(Lifecycle lifecycle,
			AbstractDistributionProvider function, AbstractPeer peer) {
		super(lifecycle, function, peer);
//		peer.bindExecutionHandler(this);
	}


	@Override
	public String getName() {
		return "BiddingProviderExecutionHandler";
	}

	@Override
	public void run() {
		if(getExecutionListenerCallback() != null) {
			getFunction().setCallback(getExecutionListenerCallback());
			getFunction().distributePlan(getExecutionListenerCallback().getQuery(), (PipeAdvertisement)getPeer().getServerResponseAddress());
		}
	}


	@Override
	public synchronized IExecutionHandler clone()
			 {
		IExecutionHandler handler = new BiddingProviderExecutionHandler(getProvidedLifecycle(), getFunction(), getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	
	
}
