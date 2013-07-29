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
 * This class provides the functionality of a single broker topology
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class SingleBrokerTopology<T extends IStreamObject<?>> extends
		AbstractBrokerTopology<T> {

	private static final String TOPOLOGY_TYPE = "SingleBroker";
	
	private IBroker<T> singleBroker;

	public SingleBrokerTopology() {
		// needed for OSGi
	}

	public SingleBrokerTopology(String domain) {
		singleBroker = new SimpleBroker<T>("RootBroker", domain);
		setDomain(domain);
	}

	/**
	 * Returns the topology Type, here 'SingleBroker'
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
	 * Returns a list of brokers, which should me addressed its a single broker
	 * topology, so this method returns the only one broker
	 * 
	 * @return list of brokers
	 */
	@Override
	public List<IBroker<T>> getAllBrokers() {
		// Only one Broker available
		List<IBroker<T>> ret = new ArrayList<IBroker<T>>();
		ret.add(singleBroker);
		return ret;
	}

	/**
	 * Returns a broker with a given name Only one Broker exists, so ignore name
	 */
	@Override
	public IBroker<T> getBrokerForSubscriber(boolean newBrokerNeeded, String subscriberUid) {
		// Single Broker architecture, ignore name
		return singleBroker;
	}

	/**
	 * Returns a new Instance of this topology Type
	 * 
	 * @param brokername
	 * @return new Instance of SingleBrokerTopology 
	 */
	@Override
	public <E extends IStreamObject<?>> IBrokerTopology<E> getInstance(
			String domain) {
		return new SingleBrokerTopology<E>(domain);
	}
}
