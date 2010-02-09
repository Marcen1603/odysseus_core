package de.uniol.inf.is.odysseus.broker.transformation;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;

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
	
}
