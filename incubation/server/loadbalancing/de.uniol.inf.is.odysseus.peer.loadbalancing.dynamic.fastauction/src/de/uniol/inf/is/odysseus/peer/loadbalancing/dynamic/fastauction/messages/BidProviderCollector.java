package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction.messages;

import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;

public class BidProviderCollector {

	ConcurrentHashMap<String,IBidProvider> bidProviders = new ConcurrentHashMap<>();
	
	
	public void bindBidProvider(IBidProvider serv) {
			if(!bidProviders.containsKey(serv.getName())) {
				bidProviders.put(serv.getName(),serv);
			}
	
	}
	
	

	
	public void unbindBidProvider(IBidProvider serv) {
			if(bidProviders.containsKey(serv.getName())) {
				bidProviders.remove(serv.getName());
			}
	
	}
	
	public IBidProvider getBidProvider(String name) {
		if(bidProviders.containsKey(name)) {
			return bidProviders.get(name);
		}
		return null;
	}
	
}
