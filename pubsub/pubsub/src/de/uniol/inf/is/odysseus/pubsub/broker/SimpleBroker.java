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

enum Filtertype{
	content,
	hierarchical,
	channel
}

public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T> {

	static Logger logger = LoggerFactory.getLogger(SimpleBroker.class);

	private Map<String, BrokerSubscription<T>> subscriptions;
	private Map<String, BrokerAdvertisements> advertisements;

	private boolean isAnyTopicHierarchical = false;
	private boolean hasAnySubscriptionTopics = false;
	private boolean hasAnySubscriptionPredicates = false;

	private Map<Filtertype, IFiltering<T>> filters = new HashMap<Filtertype, IFiltering<T>>();

	public SimpleBroker(String name, String domain) {
		super(name, domain);
		subscriptions = new HashMap<String, BrokerSubscription<T>>();
		advertisements = new HashMap<String, BrokerAdvertisements>();

		// Create and Initialize Filters
		filters.put(Filtertype.channel, new ChannelBasedFiltering<T>());
		filters.put(Filtertype.hierarchical, new HierarchicalFiltering<T>());
		filters.put(Filtertype.content, new ContentBasedFiltering<T>());
	}
	
	public SimpleBroker(String name, SimpleBroker<T> copy) {
		// Copy data
		super(name, copy.getDomain());
		subscriptions = new HashMap<String, BrokerSubscription<T>>();
		advertisements = new HashMap<String, BrokerAdvertisements>(copy.getAdvertisements());

		// Create and Initialize Filters
		filters.put(Filtertype.channel, new ChannelBasedFiltering<T>());
		filters.put(Filtertype.hierarchical, new HierarchicalFiltering<T>());
		filters.put(Filtertype.content, new ContentBasedFiltering<T>());

		// Initialize Broker
		refreshInternalStatus();
	}
	
	public Map<String, BrokerSubscription<T>> getSubscriptions() {
		return subscriptions;
	}

	public Map<String, BrokerAdvertisements> getAdvertisements() {
		return advertisements;
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		subscriptions.put(subscriber.getIdentifier(),
				new BrokerSubscription<T>(subscriber, predicates, topics));
		logger.debug("Subscriber with Identifier: '"
				+ subscriber.getIdentifier() + "' has subscribed on Broker: '"
				+ super.getName() + "' in Domain: '" + super.getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		subscriptions.remove(subscriber.getIdentifier());
		logger.debug("Subscriber with Identifier: '"
				+ subscriber.getIdentifier()
				+ "' has unsubscribed on Broker: '" + super.getName()
				+ "' in Domain: '" + super.getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public void setAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		advertisements.put(publisher.getIdentifier(), new BrokerAdvertisements(
				publisher, topics));
		logger.debug("Publisher with Identifier: '" + publisher.getIdentifier()
				+ "' has advertised on Broker: '" + super.getName()
				+ "' in Domain: '" + super.getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public void removeAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		advertisements.remove(publisher.getIdentifier());
		logger.debug("Publisher with Identifier: '" + publisher.getIdentifier()
				+ "' has unadvertised on Broker: '" + super.getName()
				+ "' in Domain: '" + super.getDomain() + "'");
		refreshInternalStatus();
	}

	@Override
	public synchronized void routeToSubscribers(T object, PublishPO<T> publisher) {

		List<String> matchedSubscriberTopics = new ArrayList<String>();
		List<String> matchedSubscriberPredicates = new ArrayList<String>();
		
		
		// Channel based filtering
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics()) {
			if (!isAnyTopicHierarchical) {
				IFiltering<T> filter = filters.get(Filtertype.channel);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
				}
				matchedSubscriberTopics.addAll(filter.filter(object, publisher));
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
				}
				matchedSubscriberTopics.addAll(filter.filter(object, publisher));
			}
			
			// if only topic based filter active
			if (!hasAnySubscriptionPredicates){
				for (String subscriberId : matchedSubscriberTopics) {
					subscriptions.get(subscriberId).getSubscriber().receive(object);
				}
			}
		}
		// Content based filtering
		if (hasAnySubscriptionPredicates) {
			IFiltering<T> filter = filters.get(Filtertype.content);
			if (filter.needsReinitialization()) {
				filter.reinitializeFilter(subscriptions.values(),
						advertisements.values());
			}
			matchedSubscriberPredicates.addAll(filter.filter(object, publisher));
			
			// if only content based filter active
			if (!hasAnySubscriptionTopics || !doesAnyPublisherAdvertiseTopics()) {
				for (String subscriberId : matchedSubscriberPredicates) {
					subscriptions.get(subscriberId).getSubscriber().receive(object);
				}
			}
		}

		// Combine Filters with logical AND if topic and content based filters active
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics() && hasAnySubscriptionPredicates) {
			for (String subscriberId : matchedSubscriberTopics) {
				if (matchedSubscriberPredicates.contains(subscriberId)){
					subscriptions.get(subscriberId).getSubscriber().receive(object);				
				}
			}			
		} 
		
		
	}

	private boolean doesAnyPublisherAdvertiseTopics() {
		return !advertisements.isEmpty();
	}

	private void refreshInternalStatus() {
		isAnyTopicHierarchical = false;
		hasAnySubscriptionTopics = false;
		hasAnySubscriptionPredicates = false;

		for (BrokerSubscription<T> subscription : subscriptions.values()) {
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
		for (BrokerAdvertisements advertisement : advertisements.values()) {
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
