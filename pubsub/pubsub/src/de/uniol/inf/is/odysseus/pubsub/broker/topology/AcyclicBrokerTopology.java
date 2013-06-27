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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.routing.IRoutingBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.routing.RoutingBrokerRegistry;

/**
 * This class provides the functionality of a acyclic broker topology
 * 
 * @author ChrisToenjesDeye
 *
 */
public class AcyclicBrokerTopology<T extends IStreamObject<?>> extends
		AbstractRoutingTopology<T> {
	
	private final String TOPOLOGY_TYPE = "AcyclicTopology";
	private Map<String, IRoutingBroker<T>> brokers = new HashMap<String, IRoutingBroker<T>>();;

	public AcyclicBrokerTopology() {
		// needed for OSGi
	}

	public AcyclicBrokerTopology(String domain) {
		setDomain(domain);
	}
	
	/**
	 * Returns a new Instance of this topology Type
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public <E extends IStreamObject<?>> IBrokerTopology<?> getInstance(
			String name) {
		return new AcyclicBrokerTopology<E>(name);
	}
	
	/**
	 * Returns the topology Type, here 'AcyclicTopology'
	 * 
	 * @return topologyType
	 */
	@Override
	public String getType() {
		return TOPOLOGY_TYPE;
	}
	
	@Override
	public void advertise(List<Topic> topics, String publisherUid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unadvertise(List<Topic> topics, String publisherUid) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns a list of brokers, which should me addressed. 
	 * 
	 * @return
	 */
	@Override
	public List<IRoutingBroker<T>> getBrokers() {
		return new ArrayList<IRoutingBroker<T>>(brokers.values());
	}

	/**
	 * Returns a broker with a given name. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	IRoutingBroker<T> getBrokerByName(String name) {
		if (brokers.containsKey(name)){
			return brokers.get(name);
		}
		return (IRoutingBroker<T>) RoutingBrokerRegistry.getRoutingBrokerInstance(getRoutingType(), getDomain(), name);
	}

	

}
