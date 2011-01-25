package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelector;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelectorFactory;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class StandardBiddingClientSelectorFactory<C extends IExecutionListenerCallback> implements IClientSelectorFactory<C> {

	@Override
	public String getName() {
		return "StandardBiddingClientSelectorFactory";
	}

	
	@Override
	public IClientSelector getNewInstance(int time, P2PQuery query, C callback, ILogListener log) {
		return new StandardBiddingClientSelector<C>(time, query, callback, log);
	}


	@Override
	public IClientSelector getNewInstance(int time, P2PQuery query, ILogListener log) {
		IClientSelector selector = new StandardBiddingClientSelector<C>(time, query, log);
		return selector;
	}


//	@Override
//	public IClientSelector getNewInstance(int time, Query query, C callback) {
//		// TODO Auto-generated method stub
//		return null;
//	}


}