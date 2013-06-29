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
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * Abstract class for broker topologies
 * 
 * @author ChrisToenjesDeye
 * 
 */
public abstract class AbstractBrokerTopology<T extends IStreamObject<?>>
		extends AbstractTopology<T> {
	
	abstract IBroker<T> getBrokerByName(String name);
	abstract List<IBroker<T>> getBrokers();

	/**
	 * subscribes a Subscriber with given Filterpredicates on the given broker
	 * 
	 * @param brokername
	 * @param list
	 * @param subscriber
	 */
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, topics, subscriber);
	}

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.removeSubscription(predicates, topics, subscriber);
	}

	@Override
	public void transfer(T object, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getBrokers();
		for (IBroker<T> broker : brokers) {
			broker.sendToSubscribers(object, publisher);
		}
	}
	
	@Override
	public boolean needsRouting(){
		return false;
	}
	
	@Override
	public void setRoutingType(String routingType){
		// no routing needed
	}

}
