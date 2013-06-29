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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.ChannelBasedFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.ContentBasedFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.HierarchicalFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.IFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

enum Filtertype {
	content, hierarchical, channel
}

public abstract class AbstractBroker<T extends IStreamObject<?>> extends
		Observable implements IBroker<T> {

	private static Logger logger = LoggerFactory
			.getLogger(AbstractBroker.class);
	private String name;
	private String domain;

	private boolean isAnyTopicHierarchical = false;
	private boolean hasAnySubscriptionTopics = false;
	private boolean hasAnySubscriptionPredicates = false;

	private Map<Filtertype, IFiltering<T>> filters = new HashMap<Filtertype, IFiltering<T>>();

	protected Map<String, BrokerSubscription<T>> subscriptions;
	protected Map<String, BrokerAdvertisements> advertisements;

	public AbstractBroker(String name, String domain) {
		this.name = name;
		this.domain = domain;
		subscriptions = new HashMap<String, BrokerSubscription<T>>();
		advertisements = new HashMap<String, BrokerAdvertisements>();

		// Create and Initialize Filters
		filters.put(Filtertype.channel, new ChannelBasedFiltering<T>());
		filters.put(Filtertype.hierarchical, new HierarchicalFiltering<T>());
		filters.put(Filtertype.content, new ContentBasedFiltering<T>());
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

	@Override
	public Map<String, BrokerAdvertisements> getAdvertisements() {
		return advertisements;
	}
	
	@Override
	public void setAdvertisements(Map<String, BrokerAdvertisements> advertisements) {
		this.advertisements = new HashMap<String, BrokerAdvertisements>(advertisements);
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
		advertisements.put(publisherUid, new BrokerAdvertisements(publisherUid,
				topics));
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

	@Override
	public synchronized void sendToSubscribers(T object, PublishPO<T> publisher) {

		List<String> matchedSubscriberTopics = new ArrayList<String>();
		List<String> matchedSubscriberPredicates = new ArrayList<String>();
		// Clear Observer list
		this.deleteObservers();

		// Channel based filtering
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics()) {
			if (!isAnyTopicHierarchical) {
				IFiltering<T> filter = filters.get(Filtertype.channel);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(getSubscriptions().values(),
							getAdvertisements().values());
				}
				matchedSubscriberTopics
						.addAll(filter.filter(object, publisher));
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(getSubscriptions().values(),
							getAdvertisements().values());
				}
				matchedSubscriberTopics
						.addAll(filter.filter(object, publisher));
			}

			// if only topic based filter active
			if (!hasAnySubscriptionPredicates) {
				for (String subscriberId : matchedSubscriberTopics) {
					super.addObserver(getSubscriptions().get(subscriberId)
							.getSubscriber());
				}
				super.setChanged();
				super.notifyObservers(object);
			}
		}
		// Content based filtering
		if (hasAnySubscriptionPredicates) {
			IFiltering<T> filter = filters.get(Filtertype.content);
			if (filter.needsReinitialization()) {
				filter.reinitializeFilter(getSubscriptions().values(),
						getAdvertisements().values());
			}
			matchedSubscriberPredicates
					.addAll(filter.filter(object, publisher));

			// if only content based filter active
			if (!hasAnySubscriptionTopics || !doesAnyPublisherAdvertiseTopics()) {
				for (String subscriberId : matchedSubscriberPredicates) {
					super.addObserver(getSubscriptions().get(subscriberId)
							.getSubscriber());
				}
				super.setChanged();
				super.notifyObservers(object);
			}
		}

		// Combine Filters with logical AND if topic and content based filters
		// active
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics()
				&& hasAnySubscriptionPredicates) {
			for (String subscriberId : matchedSubscriberTopics) {
				if (matchedSubscriberPredicates.contains(subscriberId)) {
					super.addObserver(getSubscriptions().get(subscriberId)
							.getSubscriber());
				}
			}
			super.setChanged();
			super.notifyObservers(object);
		}

	}

	private boolean doesAnyPublisherAdvertiseTopics() {
		return !getAdvertisements().isEmpty();
	}

	protected void refreshInternalStatus() {
		isAnyTopicHierarchical = false;
		hasAnySubscriptionTopics = false;
		hasAnySubscriptionPredicates = false;

		for (BrokerSubscription<T> subscription : getSubscriptions().values()) {
			if (subscription.hasTopics()) {
				hasAnySubscriptionTopics = true;
			}
			if (subscription.hasPredicates()) {
				hasAnySubscriptionPredicates = true;
			}
			for (Topic topic : subscription.getTopics()) {
				if (topic.isHierarchical()) {
					isAnyTopicHierarchical = true;
				}
			}
		}
		for (BrokerAdvertisements advertisement : getAdvertisements().values()) {
			for (Topic topic : advertisement.getTopics()) {
				if (topic.isHierarchical()) {
					isAnyTopicHierarchical = true;
				}
			}
		}

		// All filters needs reinitialization
		filters.get(Filtertype.channel).setReinitializationMode(true);
		filters.get(Filtertype.hierarchical).setReinitializationMode(true);
		filters.get(Filtertype.content).setReinitializationMode(true);
	}
}
