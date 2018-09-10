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
import java.util.UUID;

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

	// Maps Brokers and their name
	private Map<String, IRoutingBroker<T>> brokers = new HashMap<String, IRoutingBroker<T>>();

	// Maps Publisher and Brokers
	private Map<String, IRoutingBroker<T>> publishToBroker = new HashMap<String, IRoutingBroker<T>>();

	// Maps Subscribers and Brokers
	private Map<String, IRoutingBroker<T>> subscriberToBroker = new HashMap<String, IRoutingBroker<T>>();

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

	/**
	 * advertise from broker network. Advertisement will be set on all brokers
	 */
	@Override
	public void advertise(List<Topic> topics, String publisherUid) {
		getBestBroker(publisherUid).distributeAdvertisement(topics,
				publisherUid, "");
	}

	/**
	 * unadvertise from broker network. Advertisement will be deleted on all
	 * brokers
	 */
	@Override
	public void unadvertise(List<Topic> topics, String publisherUid) {
		getBestBroker(publisherUid).removeDistributedAdvertisement(topics,
				publisherUid, "");
	}

	/**
	 * returns best broker, currently its a random broker. If broker topology is
	 * distributed, return fastest broker (ping or something else)
	 */
	@Override
	public IRoutingBroker<T> getBestBroker(String publisherUid) {
		if (brokers.isEmpty()) {
			// If no Broker exists, create initial Broker
			IRoutingBroker<T> newBroker = createInitialBroker();

			// Map Publisher with Broker
			publishToBroker.put(publisherUid, newBroker);
			return newBroker;
		} else {
			if (publishToBroker.containsKey(publisherUid)) {
				// return mapped broker
				return publishToBroker.get(publisherUid);
			} else {
				// TODO If Broker Topology is distributed, don't select a random
				// Broker, select fastest broker (ping or something else)
				IRoutingBroker<T> randBroker = getRandomBroker();
				// Map Publisher with Broker, for better performance
				publishToBroker.put(publisherUid, randBroker);
				return randBroker;
			}

		}
	}

	/**
	 * Create initial broker. Needed for other brokers to create a 
	 * broker network
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IRoutingBroker<T> createInitialBroker(){
		IRoutingBroker<T> newBroker = (IRoutingBroker<T>) RoutingBrokerRegistry
		.getRoutingBrokerInstance(getRoutingType(), getDomain(),
				"InitialBroker");
		brokers.put(newBroker.getName(), newBroker);
		return newBroker;
	}

	/**
	 * Returns a broker. If newBrokerNeeded is set, a new Broker would be
	 * created otherwise the broker with minimum load is returned
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IRoutingBroker<T> getBrokerForSubscriber(boolean newBrokerNeeded,
			String subscriberUid) {
		if (brokers.isEmpty()) {
			// If no Broker exists, create initial Broker
			createInitialBroker();
		}
		if (subscriberToBroker.containsKey(subscriberUid)) {
			// return mapped broker
			return subscriberToBroker.get(subscriberUid);
		}
		if (newBrokerNeeded) {
			// Create new Broker
			IRoutingBroker<T> newBroker = (IRoutingBroker<T>) RoutingBrokerRegistry
					.getRoutingBrokerInstance(getRoutingType(), getDomain(),
							UUID.randomUUID().toString());

			// Get All existing Advertisements from other broker if exists
			if (!brokers.isEmpty()) {
				// TODO If Broker Topology is distributed, don't select a random
				// Broker, select fastest broker (ping or something else)
				IRoutingBroker<T> otherBroker = getRandomBroker();

				newBroker.setAdvertisements(otherBroker.getAdvertisements());

				// Connect Broker bidirectional
				otherBroker.getConnectedBrokers().add(newBroker);
				newBroker.getConnectedBrokers().add(otherBroker);
			}

			brokers.put(newBroker.getName(), newBroker);
			subscriberToBroker.put(subscriberUid, newBroker);
			return newBroker;
		} else {
			// TODO If Broker Topology is distributed, don't select a random
			// Broker, select fastest broker (ping or something else)
			IRoutingBroker<T> randBroker = getBrokerWithMinimumLoad();
			// Map Publisher with Broker, for better performance
			subscriberToBroker.put(subscriberUid, randBroker);
			return randBroker;
		}

	}

	/**
	 * Returns a random broker
	 * 
	 * @return routing broker
	 */
	private IRoutingBroker<T> getRandomBroker() {
		int randIndex = (int) ((brokers.keySet().size() - 1) * Math.random());
		String brokerName = new ArrayList<String>(brokers.keySet())
				.get(randIndex);
		return brokers.get(brokerName);
	}

	/**
	 * Returns a random broker
	 * 
	 * @return routing broker
	 */
	private IRoutingBroker<T> getBrokerWithMinimumLoad() {
		IRoutingBroker<T> firstBroker = brokers.values().iterator().next();
		int minimumLoad = firstBroker.getNumberOfSubscribers();
		String currentBrokerName = firstBroker.getName();

		for (IRoutingBroker<T> broker : brokers.values()) {
			if (broker.getNumberOfSubscribers() == 0) {
				return brokers.get(broker.getName());
			}
			if (broker.getNumberOfSubscribers() < minimumLoad) {
				currentBrokerName = broker.getName();
			}
		}
		return brokers.get(currentBrokerName);
	}

}
