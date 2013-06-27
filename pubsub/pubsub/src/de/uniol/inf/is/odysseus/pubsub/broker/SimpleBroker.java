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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.ChannelBasedFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.ContentBasedFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.HierarchicalFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.IFiltering;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

enum Filtertype{
	content,
	hierarchical,
	channel
}

public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T> {


	private boolean isAnyTopicHierarchical = false;
	private boolean hasAnySubscriptionTopics = false;
	private boolean hasAnySubscriptionPredicates = false;

	private Map<Filtertype, IFiltering<T>> filters = new HashMap<Filtertype, IFiltering<T>>();

	public SimpleBroker(String name, String domain) {
		super(name, domain);

		// Create and Initialize Filters
		filters.put(Filtertype.channel, new ChannelBasedFiltering<T>());
		filters.put(Filtertype.hierarchical, new HierarchicalFiltering<T>());
		filters.put(Filtertype.content, new ContentBasedFiltering<T>());
	}
	
	public SimpleBroker(String name, SimpleBroker<T> copy) {
		// Copy data
		super(name, copy.getDomain());
		getAdvertisements().putAll(copy.getAdvertisements());

		// Create and Initialize Filters
		filters.put(Filtertype.channel, new ChannelBasedFiltering<T>());
		filters.put(Filtertype.hierarchical, new HierarchicalFiltering<T>());
		filters.put(Filtertype.content, new ContentBasedFiltering<T>());

		// Initialize Broker
		refreshInternalStatus();
	}

	@Override
	public synchronized void routeToSubscribers(T object, PublishPO<T> publisher) {

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
				matchedSubscriberTopics.addAll(filter.filter(object, publisher));
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(getSubscriptions().values(),
							getAdvertisements().values());
				}
				matchedSubscriberTopics.addAll(filter.filter(object, publisher));
			}
			
			// if only topic based filter active
			if (!hasAnySubscriptionPredicates){
				for (String subscriberId : matchedSubscriberTopics) {
					super.addObserver(getSubscriptions().get(subscriberId).getSubscriber());
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
			matchedSubscriberPredicates.addAll(filter.filter(object, publisher));
			
			// if only content based filter active
			if (!hasAnySubscriptionTopics || !doesAnyPublisherAdvertiseTopics()) {
				for (String subscriberId : matchedSubscriberPredicates) {
					super.addObserver(getSubscriptions().get(subscriberId).getSubscriber());
				}
				super.setChanged();
				super.notifyObservers(object);
			}
		}

		// Combine Filters with logical AND if topic and content based filters active
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics() && hasAnySubscriptionPredicates) {
			for (String subscriberId : matchedSubscriberTopics) {
				if (matchedSubscriberPredicates.contains(subscriberId)){
					super.addObserver(getSubscriptions().get(subscriberId).getSubscriber());			
				}
				super.setChanged();
				super.notifyObservers(object);
			}			
		} 
		
		//matchedSubscriberTopics.retainAll(matchedSubscriberPredicates);
		
		
	}

	private boolean doesAnyPublisherAdvertiseTopics() {
		return !getAdvertisements().isEmpty();
	}
	
	@Override
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
