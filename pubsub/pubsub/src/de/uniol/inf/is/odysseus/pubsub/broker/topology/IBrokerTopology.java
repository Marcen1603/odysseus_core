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
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBrokerTopology<T extends IStreamObject<?>> extends Observer{
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	<E extends IStreamObject<?>> IBrokerTopology<?> getInstance(String name);
	
	/**
	 * 
	 * @return
	 */
	String getType();
	
	/**
	 * 
	 * @return
	 */
	String getDomain();
	
	/**
	 * subscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param list
	 * @param subscriber
	 */
	void subscribe(List<IPredicate<? super T>> list, List<Topic> topics , String brokerName, SubscribePO<T> subscriber);

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void unsubscribe(List<IPredicate<? super T>> predicates, List<Topic> topics , String brokerName, SubscribePO<T> subscriber);
	
	
	/**
	 * the publisher uses the transfer method to publish the content to the brokernetwork
	 * and to all subscribers
	 */
	void transfer(T object, PublishPO<T> publisher);
	
	/**
	 * Finds the best Broker
	 * @return
	 */
	List<IBroker<T>> getBrokers();

	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void advertise(List<Topic> topics, String publisherUid);

	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void unadvertise(List<Topic> topics, String publisherUid);

	/**
	 * 
	 */
	void incrementNumberOfAgents();

	/**
	 * 
	 */
	void decrementNumberOfAgents();

	/**
	 * 
	 * @return
	 */
	boolean hasAgents();
}
