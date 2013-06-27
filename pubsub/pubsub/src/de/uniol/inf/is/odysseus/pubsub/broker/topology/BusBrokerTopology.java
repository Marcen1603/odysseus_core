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
import java.util.List;
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
	private final String TOPOLOGY_TYPE = "BusTopology";
	private List<IBroker<T>> brokerBus = new ArrayList<IBroker<T>>();;

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
		List<IBroker<T>> brokers = getBrokers();
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
		List<IBroker<T>> brokers = getBrokers();
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
	public List<IBroker<T>> getBrokers() {
		// It's a Bus, so every Broker needs to be addressed
		return brokerBus;
	}

	/**
	 * Returns a broker with a given name. If broker not exists create new one
	 * and copy all advertisements from mainBroker
	 */
	@Override
	IBroker<T> getBrokerByName(String name) {
		// Find Broker in existing Bus
		for (IBroker<T> broker : brokerBus) {
			if (broker.getName().toLowerCase().equals(name.toLowerCase())) {
				return broker;
			}
		}
		// If Broker with given name does not exist, create new one (copy from
		// existing) and return it
		SimpleBroker<T> mainBroker = (SimpleBroker<T>) getBrokerByName(MAIN_BROKER);
		IBroker<T> newBroker = new SimpleBroker<T>(name, mainBroker);
		brokerBus.add(newBroker);
		return newBroker;
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
