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

package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public abstract class AbstractBroker<T extends IStreamObject<?>> extends Observable implements IBroker<T>{
	
	protected abstract void refreshInternalStatus();
	
	private static Logger logger = LoggerFactory.getLogger(AbstractBroker.class);

	private String name;
	private String domain;
	
	private Map<String, BrokerSubscription<T>> subscriptions;
	private Map<String, BrokerAdvertisements> advertisements;
	
	public AbstractBroker(String name, String domain){
		this.name = name;
		this.domain = domain;
		subscriptions = new HashMap<String, BrokerSubscription<T>>();
		advertisements = new HashMap<String, BrokerAdvertisements>();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDomain() {
		return domain;
	}
	
	public Map<String, BrokerSubscription<T>> getSubscriptions() {
		return subscriptions;
	}

	public Map<String, BrokerAdvertisements> getAdvertisements() {
		return advertisements;
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		subscriptions.put(subscriber.getIdentifier(),
				new BrokerSubscription<T>(subscriber, predicates, topics));
		logger.debug("Subscriber with Identifier: '"
				+ subscriber.getIdentifier() + "' has subscribed on Broker: '"
				+ getName() + "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		subscriptions.remove(subscriber.getIdentifier());
		logger.debug("Subscriber with Identifier: '"
				+ subscriber.getIdentifier()
				+ "' has unsubscribed on Broker: '" + getName()
				+ "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public void setAdvertisement(List<Topic> topics, String publisherUid) {
		advertisements.put(publisherUid, new BrokerAdvertisements(
				publisherUid, topics));
		logger.debug("Publisher with Identifier: '" + publisherUid
				+ "' has advertised on Broker: '" + getName()
				+ "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();
		
	}

	@Override
	public void removeAdvertisement(List<Topic> topics, String publisherUid) {
		advertisements.remove(publisherUid);
		logger.debug("Publisher with Identifier: '" + publisherUid
				+ "' has unadvertised on Broker: '" + getName()
				+ "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();
		
	}

	@Override
	public boolean hasSubscriptions() {
		return !subscriptions.isEmpty();
	}
}
