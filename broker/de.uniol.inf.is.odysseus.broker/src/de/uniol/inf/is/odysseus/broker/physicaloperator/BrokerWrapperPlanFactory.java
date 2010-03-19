package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BrokerWrapperPlanFactory {

	private static Map<String, BrokerPO<?>> sources = new HashMap<String, BrokerPO<?>>();

	public synchronized static BrokerPO<?> getPlan(String uri) {
		BrokerPO<?> po = sources.get(uri);
		return po;
	}

	public synchronized static void putPlan(String uri, BrokerPO<?> s) {
		sources.put(uri, s);
	}
		
	public synchronized static Map<String, BrokerPO<?>> getBroker() {
		return sources;
	}

	public synchronized static void clearSources() {
		sources.clear();
	}
	
	public synchronized static boolean isEmpty(){
		return sources.isEmpty();
	}
	
	public synchronized static Map<String, BrokerPO<?>> getAll(){
		return sources;
	}
	
	public synchronized static List<BrokerPO<?>> getAllBrokerPOs(){
		List<BrokerPO<?>> liste = new ArrayList<BrokerPO<?>>();
		for(String key :sources.keySet()){
			liste.add(sources.get(key));
		}
		return liste;
	}
	
}
