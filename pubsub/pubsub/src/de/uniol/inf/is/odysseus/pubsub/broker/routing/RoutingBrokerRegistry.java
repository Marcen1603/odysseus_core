package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;

public class RoutingBrokerRegistry {

static Logger logger = LoggerFactory.getLogger(BrokerTopologyRegistry.class);
	
	// Maps Type and the corresponding Broker
	static Map<String, IRoutingBroker<?>> routingBrokers = new HashMap<String, IRoutingBroker<?>>();
	
	
	/**
	 * OSGi method - registers all topologies which implements IBrokerTopology
	 * @param routingBroker
	 */
	public static void registerRoutingBrokers(IRoutingBroker<?> routingBroker) {
		logger.debug("Register new Routing Broker " + routingBroker.getType());
		if (!routingBrokers.containsKey(routingBroker.getType().toLowerCase())) {
			routingBrokers.put(routingBroker.getType().toLowerCase(), routingBroker);
		} else {
			logger.error("Routing Broker with type " + routingBroker.getType()
					+ " already registered");
		}
	}
	
	/**
	 * OSGi method - unregisters all topologies which implements IBrokerTopology
	 * @param routingBroker
	 */
	public static void unregisterRoutingBrokers(IRoutingBroker<?> routingBroker) {
		logger.debug("Remove Routing Broker "+routingBroker.getType());
		routingBrokers.remove(routingBroker.getType().toLowerCase());
	}
	
	public static IRoutingBroker<?> getRoutingBrokerInstance(String type, String domain, String name){
		if (routingBrokers.containsKey(type.toLowerCase())){
			return routingBrokers.get(type.toLowerCase()).getInstance(name, domain);
		}
		return null;
	}
	
	public static List<String> getValidRoutingTypes(){
		return new ArrayList<String>(routingBrokers.keySet());
	}
}
