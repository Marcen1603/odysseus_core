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
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.routing.IRoutingBroker;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * Abstract class for broker topologies, which need routing brokers
 * 
 * @author ChrisToenjesDeye
 * 
 */
public abstract class AbstractRoutingTopology<T extends IStreamObject<?>>
		extends AbstractTopology<T> {

	private String routingType;

	
	abstract IRoutingBroker<T> getBrokerForSubscriber(boolean newBrokerNeeded, String subscriberUid);

	abstract IRoutingBroker<T> getBestBroker(String publisherUid);
	

	/**
	 * subscribes a Subscriber with given filter on the given broker
	 * 
	 * @param brokername
	 * @param list
	 * @param subscriber
	 */
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, boolean newBrokerNeeded, SubscribePO<T> subscriber) {
		IRoutingBroker<T> broker = getBrokerForSubscriber(newBrokerNeeded, subscriber.getIdentifier());
		broker.setSubscription(predicates, topics, subscriber);
	}

	/**
	 * unsubscribes a Subscriber with given filter on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, boolean newBrokerNeeded, SubscribePO<T> subscriber) {
		IRoutingBroker<T> broker = getBrokerForSubscriber(newBrokerNeeded, subscriber.getIdentifier());
		broker.removeSubscription(predicates, topics, subscriber);
	}
	
	/**
	 * transfers object to first broker. Object will distributed via route-method
	 */
	@Override
	public void transfer(T object, String publisherUid) {
		getBestBroker(publisherUid).route(object, publisherUid, "");
	}
	
	/**
	 * returns true, because its a routing topology
	 */
	@Override
	public boolean needsRouting(){
		return true;
	}
	
	/**
	 * set routing type
	 */
	@Override
	public void setRoutingType(String routingType){
		this.routingType = routingType;
	}
	
	/**
	 * get routing type
	 * @return routing type
	 */
	public String getRoutingType(){
		return routingType;
	}

}
