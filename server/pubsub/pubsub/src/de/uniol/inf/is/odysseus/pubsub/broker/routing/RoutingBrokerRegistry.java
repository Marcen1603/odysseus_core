/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;

/**
 * This class manages all routing broker types
 * 
 * @author ChrisToenjesDeye
 *
 */
public class RoutingBrokerRegistry {

	private static Logger logger = LoggerFactory.getLogger(BrokerTopologyRegistry.class);
	
	// Maps Type and the corresponding Broker
	private static Map<String, IRoutingBroker<?>> routingBrokers = new HashMap<String, IRoutingBroker<?>>();
	
	
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
	
	/**
	 * Returns a new instance of a rounting broker, if type is valid.
	 * @param type
	 * @param domain
	 * @param name
	 * @return a new instance of a routing broker
	 */
	public static IRoutingBroker<?> getRoutingBrokerInstance(String type, String domain, String name){
		if (routingBrokers.containsKey(type.toLowerCase())){
			return routingBrokers.get(type.toLowerCase()).getInstance(name, domain);
		}
		return null;
	}
	
	/**
	 * returns valid routing types
	 */
	public static List<String> getValidRoutingTypes(){
		return new ArrayList<String>(routingBrokers.keySet());
	}
}
