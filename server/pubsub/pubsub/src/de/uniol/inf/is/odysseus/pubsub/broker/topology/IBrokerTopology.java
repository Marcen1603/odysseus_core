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

import java.util.List;
import java.util.Observer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * Interface for Broker Topologies
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IBrokerTopology<T extends IStreamObject<?>> extends Observer{
	
	/**
	 * Returns a new Instance of this topology Type
	 * 
	 * @param name
	 * @return
	 */
	<E extends IStreamObject<?>> IBrokerTopology<?> getInstance(String name);
	
	/**
	 * Returns the topology Type
	 * 
	 * @return topologyType
	 */
	String getType();
	
	/**
	 * Returns the domain of the topolgy
	 * 
	 * @return domain
	 */
	String getDomain();
	
	/**
	 * subscribes a Subscriber with given Filterpredicates on the given broker
	 * 
	 * @param brokername
	 * @param list
	 * @param subscriber
	 */
	void subscribe(List<IPredicate<? super T>> list, List<Topic> topics , boolean newBrokerNeeded, SubscribePO<T> subscriber);

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void unsubscribe(List<IPredicate<? super T>> predicates, List<Topic> topics , boolean newBrokerNeeded, SubscribePO<T> subscriber);

	/**
	 * advertise on brokers
	 * 
	 * @param topics
	 * @param publisher
	 */
	void advertise(List<Topic> topics, String publisherUid);

	/**
	 * unadvertise in brokers
	 * 
	 * @param topics
	 * @param publisher
	 */
	void unadvertise(List<Topic> topics, String publisherUid);

	/**
	 * increment the number of agents, which are registered on this topology
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 */
	void incrementNumberOfAgents();

	/**
	 * decrement the number of agents, which are registered on this topology
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 */
	void decrementNumberOfAgents();

	/**
	 * returns of the topology has any registered agents (publisher or subscriber)
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 * @return
	 */
	boolean hasAgents();

	/**
	 * transfers the object to broker
	 * @param object
	 * @param publisher
	 */
	void transfer(T object, String publisherUid);
	
	/**
	 * checks if the given topology needs a routing algorithm
	 * @return needs routing
	 */
	boolean needsRouting();

	/**
	 * sets the routing type
	 * @param routingType
	 */
	void setRoutingType(String routingType);
}
