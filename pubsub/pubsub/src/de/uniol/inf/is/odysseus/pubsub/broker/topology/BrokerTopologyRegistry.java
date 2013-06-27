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

package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * This class manages all broker topologies for publish/subscribe functionality
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BrokerTopologyRegistry {

	static Logger logger = LoggerFactory.getLogger(BrokerTopologyRegistry.class);
	
	// Maps Type and the corresponding Topology
	static Map<String, IBrokerTopology<?>> brokerTopologyTypes = new HashMap<String, IBrokerTopology<?>>();
	
	// Maps Domainname and the corresponding Topology
	static Map<String, IBrokerTopology<?>> brokerTopologies = new HashMap<String, IBrokerTopology<?>>();
	
	// Pending Subscribers
	static Map<String, List<SubscribePO<?>>> pendingSubscribers = new HashMap<String, List<SubscribePO<?>>>();
	
	
	/**
	 * OSGi method - registers all topologies which implements IBrokerTopology
	 * @param brokerTopology
	 */
	public static void registerBrokertopologies(IBrokerTopology<?> brokerTopology) {
		logger.debug("Register new Broker Topology " + brokerTopology.getType());
		if (!brokerTopologyTypes.containsKey(brokerTopology.getType().toLowerCase())) {
			brokerTopologyTypes.put(brokerTopology.getType().toLowerCase(), brokerTopology);
		} else {
			logger.error("Broker Topology with name " + brokerTopology.getType()
					+ " already registered");
		}
	}
	
	/**
	 * OSGi method - unregisters all topologies which implements IBrokerTopology
	 * @param brokerTopology
	 */
	public static void unregisterBrokertopologies(IBrokerTopology<?> brokerTopology) {
		logger.debug("Remove Broker Topology "+brokerTopology.getType());
		brokerTopologyTypes.remove(brokerTopology.getType().toLowerCase());
	}
	
	/**
	 * Returns a list of valid topology types
	 * @return
	 */
	public static List<String> getValidTopologyTypes(){
		return new ArrayList<String>(brokerTopologyTypes.keySet());
	}
	
	public static boolean needsTopologyRouting(String topologyType){
		return brokerTopologyTypes.get(topologyType.toLowerCase()).needsRouting();
	}
	
	/**
	 * checks if a combination of domain and type is valid. needed for validation
	 * @param topologyType
	 * @param domain
	 * @return is valid?
	 */
	public static boolean isDomainTypeCombinationValid(String topologyType, String domain){
		if (brokerTopologies.containsKey(domain.toLowerCase())){
			IBrokerTopology<?> topology = brokerTopologies.get(domain.toLowerCase());
			if (!topology.getType().toLowerCase().equals(topologyType.toLowerCase())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param topologyType
	 * @param domain
	 * @return topology with given domain and type, if combination of domain and type is valid
	 */
	public static <T extends IStreamObject<?>> IBrokerTopology<?> getTopologyByTypeAndDomain(String topologyType, String domain, String routing){
		// Check if topology Type is valid
		if (!brokerTopologyTypes.containsKey(topologyType.toLowerCase())){
			logger.info("Topology Type: '"+ topologyType + "' is not valid.");
			return null;
		}
		
		if (brokerTopologies.containsKey(domain.toLowerCase())){
			IBrokerTopology<?> topology = brokerTopologies.get(domain.toLowerCase());
			if (topology.getType().toLowerCase().equals(topologyType.toLowerCase())){
				// Broker with type and domain exists
				if (pendingSubscribers.containsKey(domain.toLowerCase())){
					for (SubscribePO<?> pendingSubscriber : pendingSubscribers.get(domain.toLowerCase())) {
						pendingSubscriber.subscribe(topology);
					}
					pendingSubscribers.remove(domain.toLowerCase());
				}
				return topology;				
			} else {
				// Broker with domain exists, but has a different type
				logger.info("Domain: '"+ domain +"' already exists with a different topology type. It's not possible to register a Topology with the same domain and a different topologyType.");
				return null;
			}
		} else {
			// Broker with type and domain does not exists, create new Instance
			IBrokerTopology<?> topology = brokerTopologyTypes.get(topologyType.toLowerCase());
			IBrokerTopology<?> ret = topology.<T>getInstance(domain.toLowerCase());
			if (ret.needsRouting()){
				ret.setRoutingType(routing);
			}
			brokerTopologies.put(domain.toLowerCase(), ret);
			
			if (pendingSubscribers.containsKey(domain.toLowerCase())){
				for (SubscribePO<?> pendingSubscriber : pendingSubscribers.get(domain.toLowerCase())) {
					pendingSubscriber.subscribe(ret);
				}
				pendingSubscribers.remove(domain.toLowerCase());
			}		
			return ret;
		}
	}
	
	/**
	 * 
	 * @param domain
	 * @return topology with a given name 
	 */
	public static IBrokerTopology<?> getTopologyByDomain(String domain) {
		// Check if domain exists
		if (brokerTopologies.containsKey(domain.toLowerCase())){
			return brokerTopologies.get(domain.toLowerCase());			
		} else {
			logger.info("Topology with domain: '"+ domain + "' does not exists. Add to pending subscriber list.");
			return null;
		}
	}
	
	/**
	 * 
	 * @param domain
	 * @param subscriber
	 */
	public static void putSubscriberIntoPendingList(String domain, SubscribePO<?> subscriber){
		if (pendingSubscribers.containsKey(domain.toLowerCase())){
			pendingSubscribers.get(domain.toLowerCase()).add(subscriber);			 
		} else {
			ArrayList<SubscribePO<?>> newSubscribers = new ArrayList<SubscribePO<?>>();
			newSubscribers.add(subscriber);
			pendingSubscribers.put(domain.toLowerCase(), newSubscribers);
		}
	}

	/**
	 * 
	 * @param domain
	 */
	public static void register(String domain) {
		IBrokerTopology<?> topology = getTopologyByDomain(domain);
		if (topology != null){
			topology.incrementNumberOfAgents();
		}
	}

	/**
	 * 
	 * @param domain
	 */
	public static void unregister(String domain) {
		IBrokerTopology<?> topology = getTopologyByDomain(domain);
		if (topology != null){
			topology.decrementNumberOfAgents();
			if (!topology.hasAgents()){
				// Remove Topology with given name
				brokerTopologies.remove(domain.toLowerCase());
				if (pendingSubscribers.containsKey(domain)){
					pendingSubscribers.remove(domain);
				}
			}			
		}
	}



}
