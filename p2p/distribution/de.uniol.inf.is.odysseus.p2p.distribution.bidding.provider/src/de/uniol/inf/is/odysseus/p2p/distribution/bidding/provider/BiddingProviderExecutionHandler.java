package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class BiddingProviderExecutionHandler
		extends
		AbstractExecutionHandler<AbstractDistributionProvider<? super PipeAdvertisement>> {

	public BiddingProviderExecutionHandler(Lifecycle lifecycle,
			AbstractDistributionProvider<? super PipeAdvertisement> function,
			IOdysseusPeer peer) {
		super(lifecycle, function, peer);
	}

	public BiddingProviderExecutionHandler(
			BiddingProviderExecutionHandler biddingProviderExecutionHandler) {
		super(biddingProviderExecutionHandler);
	}

	@Override
	public String getName() {
		return "BiddingProviderExecutionHandler";
	}

	@Override
	public void run() {
		if (getExecutionListenerCallback() != null) {
			getFunction().setCallback(getExecutionListenerCallback());
			getFunction().distributePlan(
					getExecutionListenerCallback().getQuery(),
					(PipeAdvertisement) getPeer().getServerResponseAddress());
		}
	}

	@Override
	public synchronized BiddingProviderExecutionHandler clone() {
		return new BiddingProviderExecutionHandler(this);
	}

}
