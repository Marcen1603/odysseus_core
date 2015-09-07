package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction;

import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;

public class BidProviderCollector {

	private static ConcurrentHashMap<String,IBidProvider> bidProviders = new ConcurrentHashMap<>();
	
	
	public static void bindBidProvider(IBidProvider serv) {
			if(!bidProviders.containsKey(serv.getName())) {
				bidProviders.put(serv.getName(),serv);
			}
	
	}
	
	
	public static void unbindBidProvider(IBidProvider serv) {
			if(bidProviders.containsKey(serv.getName())) {
				bidProviders.remove(serv.getName());
			}
	
	}
	
	public static IBidProvider getBidProvider(String name) {
		if(bidProviders.containsKey(name)) {
			return bidProviders.get(name);
		}
		return null;
	}
	
	public static boolean isBidProviderBound(String name) {
		return bidProviders.containsKey(name);
	}
	
}
