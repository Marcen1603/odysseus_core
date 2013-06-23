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

public abstract class AbstractBrokerTopology<T extends IStreamObject<?>>
		implements IBrokerTopology<T> {

	abstract IBroker<T> getBrokerByName(String name);

	private String domainName;
	
	private int numberOfAgents = 0;

	@Override
	public String getDomain() {
		return domainName;
	}

	public void setDomain(String domain) {
		this.domainName = domain;
	}
	
	@Override
	public void incrementNumberOfAgents(){
		numberOfAgents++;
	}
	
	@Override
	public void decrementNumberOfAgents(){
		numberOfAgents--;
	}
	
	public boolean hasAgents(){
		if (numberOfAgents > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, topics, subscriber);
	}

	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.removeSubscription(predicates, topics, subscriber);
	}

	@Override
	public void advertise(List<Topic> topics, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getBrokers();
		for (IBroker<T> broker : brokers) {
			broker.setAdvertisement(topics, publisher);
		}
	}
	
	@Override
	public void unadvertise(List<Topic> topics, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getBrokers();
		for (IBroker<T> broker : brokers) {
			broker.removeAdvertisement(topics, publisher);
		}
	}

	@Override
	public void transfer(T object, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getBrokers();
		for (IBroker<T> broker : brokers) {
			broker.routeToSubscribers(object, publisher);
		}
	}

}
