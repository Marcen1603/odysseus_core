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
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.SimpleBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;

/**
 * This class provides the functionality of a bus broker topology
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class BusBrokerTopology<T extends IStreamObject<?>> extends
		AbstractBrokerTopology<T> {

	private static final String MAIN_BROKER = "MainBroker";
	private static final String TOPOLOGY_TYPE = "BusTopology";

	private List<IBroker<T>> brokerBus = new ArrayList<IBroker<T>>();

	private Map<String, String> subscriberBrokerMapping = new HashMap<String, String>();

	public BusBrokerTopology() {
		// needed for OSGi
	}

	public BusBrokerTopology(String domain) {
		setDomain(domain);
		brokerBus.add(new SimpleBroker<T>(MAIN_BROKER, domain));
	}

	/**
	 * Returns the topology Type, here 'BusTopology'
	 * 
	 * @return topologyType
	 */
	@Override
	public String getType() {
		return TOPOLOGY_TYPE;
	}

	/**
	 * advertise on brokers
	 * 
	 * @param topics
	 * @param publisher
	 */
	@Override
	public void advertise(List<Topic> topics, String publisherUid) {
		List<IBroker<T>> brokers = getAllBrokers();
		for (IBroker<T> broker : brokers) {
			broker.setAdvertisement(topics, publisherUid);
		}
	}

	/**
	 * unadvertise in brokers
	 * 
	 * @param topics
	 * @param publisher
	 */
	@Override
	public void unadvertise(List<Topic> topics, String publisherUid) {
		List<IBroker<T>> brokers = getAllBrokers();
		for (IBroker<T> broker : brokers) {
			broker.removeAdvertisement(topics, publisherUid);
		}
	}

	/**
	 * Returns a list of brokers, which should me addressed. It is a Bus, so
	 * every Broker needs to be addressed
	 * 
	 * @return
	 */
	@Override
	public List<IBroker<T>> getAllBrokers() {
		// It's a Bus, so every Broker needs to be addressed
		return brokerBus;
	}

	/**
	 * Returns a broker. If newBrokerNeeded is set, a new Broker would be
	 * created otherwise the broker with minimum load is returned
	 */
	@Override
	public IBroker<T> getBrokerForSubscriber(boolean newBrokerNeeded,
			String subscriberUid) {
		if (subscriberBrokerMapping.containsKey(subscriberUid)) {
			String brokername = subscriberBrokerMapping.get(subscriberUid);
			return getBrokerByName(brokername);
		}
		if (newBrokerNeeded) {
			// If Broker with given name does not exist, create new one (copy
			// from
			// existing) and return it
			SimpleBroker<T> mainBroker = (SimpleBroker<T>) getBrokerByName(MAIN_BROKER);
			IBroker<T> newBroker = new SimpleBroker<T>(UUID.randomUUID()
					.toString(), mainBroker);
			brokerBus.add(newBroker);
			subscriberBrokerMapping.put(subscriberUid, newBroker.getName());
			return newBroker;
		} else {
			IBroker<T> bestBroker = getBrokerWithMinimumLoad();
			subscriberBrokerMapping.put(subscriberUid, bestBroker.getName());
			return bestBroker;
		}
	}

	/**
	 * Returns the broker with the minimum number of Subscribers
	 * 
	 * @return broker
	 */
	private IBroker<T> getBrokerWithMinimumLoad() {
		int minimumLoad = brokerBus.get(0).getNumberOfSubscribers();
		IBroker<T> currentBroker = brokerBus.get(0);
		for (IBroker<T> broker : brokerBus) {
			if (broker.getNumberOfSubscribers() == 0) {
				return broker;
			}
			if (broker.getNumberOfSubscribers() < minimumLoad) {
				currentBroker = broker;
			}
		}
		return currentBroker;
	}

	/**
	 * Returns the broker with given name
	 * 
	 * @return broker
	 */
	private IBroker<T> getBrokerByName(String name) {
		// Find Broker in existing Bus
		for (IBroker<T> broker : brokerBus) {
			if (broker.getName().toLowerCase().equals(name.toLowerCase())) {
				return broker;
			}
		}
		return null;
	}

	/**
	 * Returns a new Instance of this topology Type
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public <E extends IStreamObject<?>> IBrokerTopology<E> getInstance(
			String domain) {
		return new BusBrokerTopology<E>(domain);
	}

}
