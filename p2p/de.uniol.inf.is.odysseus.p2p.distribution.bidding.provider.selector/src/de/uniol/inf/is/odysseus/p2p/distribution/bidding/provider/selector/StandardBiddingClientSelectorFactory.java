package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.selector;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.selector.IClientSelector;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.selector.IClientSelectorFactory;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;

public class StandardBiddingClientSelectorFactory implements IClientSelectorFactory {

	@Override
	public String getName() {
		return "StandardBiddingClientSelectorFactory";
	}

	@Override
	public IClientSelector getNewInstance(int time,
			IExecutionListenerCallback callback) {
		return new StandardBiddingClientSelector(time, callback);
	}


}
