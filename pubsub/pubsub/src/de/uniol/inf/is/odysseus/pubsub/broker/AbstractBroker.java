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
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * enum for registered filtertypes
 * 
 * @author ChrisToenjesDeye
 *
 */
enum Filtertype {
	content, hierarchical, channel
}

/**
 * base abstract class, that provides base broker functionality
 * 
 * @author ChrisToenjesDeye
 * 
 */
public abstract class AbstractBroker<T extends IStreamObject<?>> extends
		Observable implements IBroker<T> {
	
	// Logger
	private static Logger logger = LoggerFactory
			.getLogger(AbstractBroker.class);
	
	// name and domain of the broker
	private String name;
	private String domain;

	// status variables for efficient filtering
	private boolean isAnyTopicHierarchical = false;
	private boolean hasAnySubscriptionTopics = false;
	private boolean hasAnySubscriptionPredicates = false;

	// Maps filtername and filter
	private Map<Filtertype, IFiltering<T>> filters = new HashMap<Filtertype, IFiltering<T>>();

	// Maps subscriberUid and list of subscriptions
	protected Map<String, BrokerSubscription<T>> subscriptions;
	
	// Maps publisherUid and list of advertisements
	protected Map<String, BrokerAdvertisements> advertisements;

	private static long endTime;

	private static long startTime;

	
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

	@Override
	public Map<String, BrokerAdvertisements> getAdvertisements() {
		return advertisements;
	}

	@Override
	public void setAdvertisements(
			Map<String, BrokerAdvertisements> advertisements) {
		this.advertisements = new HashMap<String, BrokerAdvertisements>(
				advertisements);
	}

	/**
	 * sets predicates and topics for a given subscriber
	 * @param predicates
	 * @param topics
	 * @param subscriber
	 */
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

	/**
	 * removes predicates and topics for a given subscriber
	 * @param predicates
	 * @param topics
	 * @param subscriber
	 */
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

	/**
	 * sets topics for a given publisher
	 * @param topics
	 * @param publisher
	 */
	@Override
	public void setAdvertisement(List<Topic> topics, String publisherUid) {
		advertisements.put(publisherUid, new BrokerAdvertisements(publisherUid,
				topics));
		logger.debug("Publisher with Identifier: '" + publisherUid
				+ "' has advertised on Broker: '" + getName()
				+ "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();

	}

	/**
	 * removes topics for a given publisher
	 * @param topics
	 * @param publisher
	 */
	@Override
	public void removeAdvertisement(List<Topic> topics, String publisherUid) {
		advertisements.remove(publisherUid);
		logger.debug("Publisher with Identifier: '" + publisherUid
				+ "' has unadvertised on Broker: '" + getName()
				+ "' in Domain: '" + getDomain() + "'");
		refreshInternalStatus();

	}

	/**
	 * filters the object with content based filtering and topic based filtering (if subscriptions and advertisements available)
	 * @param object
	 */
	@Override
	public synchronized void sendToSubscribers(T object, String publisherUid) {
		List<String> matchedSubscriberTopics = new ArrayList<String>();
		List<String> matchedSubscriberPredicates = new ArrayList<String>();
		
		logger.debug("Filtering for Message"+object.hashCode());
		
		// Clear Observer list
		this.deleteObservers();

		if (hasAnySubscriptionTopics) {
			// Topic based filtering
			if (!isAnyTopicHierarchical) {
				// Channel based filtering
				IFiltering<T> filter = filters.get(Filtertype.channel);
				startTimer();
				boolean withOptimization = false;
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
					withOptimization = true;
				}
				matchedSubscriberTopics
						.addAll(filter.filter(object, publisherUid));
				stopTimer(Filtertype.channel.toString(), withOptimization);
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				startTimer();
				boolean withOptimization = false;
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
					withOptimization = true;
				}
				matchedSubscriberTopics
						.addAll(filter.filter(object, publisherUid));
				stopTimer(Filtertype.hierarchical.toString(), withOptimization);
			}

			// if only topic based filter active
			if (!hasAnySubscriptionPredicates) {
				// add observers (subscribers) and notify them
				for (String subscriberId : matchedSubscriberTopics) {
					super.addObserver(subscriptions.get(subscriberId)
							.getSubscriber());
				}
				super.setChanged();
				super.notifyObservers(object);
			}
		}
		// Content based filtering
		if (hasAnySubscriptionPredicates) {
			IFiltering<T> filter = filters.get(Filtertype.content);
			startTimer();
			boolean withOptimization = false;
			if (filter.needsReinitialization()) {
				filter.reinitializeFilter(subscriptions.values(),
						advertisements.values());
				withOptimization = true;
			}
			matchedSubscriberPredicates
					.addAll(filter.filter(object, publisherUid));
			stopTimer(Filtertype.content.toString(), withOptimization);

			// if only content based filter active
			if (!hasAnySubscriptionTopics) {
				// add observers (subscribers) and notify them
				for (String subscriberId : matchedSubscriberPredicates) {
					super.addObserver(subscriptions.get(subscriberId)
							.getSubscriber());
				}
				super.setChanged();
				super.notifyObservers(object);
			}
		}

		// Combine Filters with logical AND if topic and content based filters
		// active
		if (hasAnySubscriptionTopics && hasAnySubscriptionPredicates) {
			for (String subscriberId : matchedSubscriberTopics) {
				if (matchedSubscriberPredicates.contains(subscriberId)) {
					// if both filters match, add subscriber to observers
					super.addObserver(subscriptions.get(subscriberId)
							.getSubscriber());
				}
			}
			// notify all subscribers
			super.setChanged();
			super.notifyObservers(object);
		}
	}

	/**
	 * refreshes the internal status. This should be done if
	 * subscriptions or advertisements changed
	 * The internal status is needed for better performance
	 */
	protected void refreshInternalStatus() {
		isAnyTopicHierarchical = false;
		hasAnySubscriptionTopics = false;
		hasAnySubscriptionPredicates = false;

		// Checks if Subscriptions has topics or predicates or both
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
		// checks if any topic is hierarchical
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
	
	@Override
	public int getNumberOfSubscribers(){
		return subscriptions.values().size();
	}
	
	public static void startTimer(){
		// Reset first
		startTime = 0;
	    endTime = 0;
		
		startTime = System.nanoTime();
	}
	
	public static void stopTimer(String filtername, boolean withOptimization) {
		endTime = System.nanoTime();
		
		long nanoseconds = (endTime - startTime) / 1000;
	    if (withOptimization){
	    	logger.debug(filtername+" Filtering needs with Optimization "+nanoseconds+" Nanoseconds");	
	    } else {
	    	logger.debug(filtername+" Filter needs "+nanoseconds+" Nanoseconds");	    	
	    }
	    
	    // Reset after
	    startTime = 0;
	    endTime = 0;
	}

}
