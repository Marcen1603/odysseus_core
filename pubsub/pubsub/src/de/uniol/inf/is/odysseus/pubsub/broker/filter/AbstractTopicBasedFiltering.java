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

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;

/**
 * abstract class for topic based filtering
 * 
 * @author ChrisToenjesDeye
 * 
 */
public abstract class AbstractTopicBasedFiltering<T extends IStreamObject<?>>
		extends AbstractFiltering<T> {

	abstract boolean hasSubscriptionTopic(Topic topic,
			BrokerSubscription<T> subscription);

	// matches publisherUid with a list of subscriberUids for better performace
	Map<String, List<String>> matches = new HashMap<String, List<String>>();

	private Collection<BrokerSubscription<T>> subscriptions = new ArrayList<BrokerSubscription<T>>();

	/**
	 * reinitialize the filter for better performance. after initialization the
	 * filter method only need a lookup in the matches map
	 */
	@Override
	public void reinitializeFilter(
			Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.subscriptions = subscriptions;
		this.matches.clear();

		for (BrokerSubscription<T> subscription : subscriptions) {
			for (BrokerAdvertisements advertisement : advertisements) {
				boolean addSubscriberToPublisher = true;

				// if advertisement is more concrete, subscriber is not
				// interested
				if (advertisement.getTopics().size() > subscription.getTopics()
						.size() && subscription.getTopics().size() > 0) {
					addSubscriberToPublisher = false;
				}

				// if subscriber has no topics, subscriber is interested on
				// content from all publishers
				if (subscription.hasTopics()) {
					for (Topic topic : advertisement.getTopics()) {
						if (!hasSubscriptionTopic(topic, subscription)) {
							addSubscriberToPublisher = false;
							break;
						}
					}
				}

				if (matches.containsKey(advertisement.getPublisherUid())) {
					// If publisher already has assigned subscribers
					if (!matches.get(advertisement.getPublisherUid()).contains(
							subscription.getSubscriber().getIdentifier())) {
						if (addSubscriberToPublisher) {
							matches.get(advertisement.getPublisherUid()).add(
									subscription.getSubscriber()
											.getIdentifier());
						}
					}
				} else {
					// If publisher has no assigned subscribers
					List<String> matchedSubscriber = new ArrayList<String>();
					if (addSubscriberToPublisher) {
						matchedSubscriber.add(subscription.getSubscriber()
								.getIdentifier());
					}
					matches.put(advertisement.getPublisherUid(),
							matchedSubscriber);
				}

			}
		}
		setReinitializationMode(false);
	}

	/**
	 * topic based filtering. only a lookup into matches map is needed after
	 * filter is initialized.
	 */
	@Override
	public List<String> filter(T object, String publisherUid) {
		if (matches.containsKey(publisherUid)) {
			return matches.get(publisherUid);
		} else {
			// if publisher has no advertisements,
			// check if subscribers
			// exists which has no topics and add them to result
			ArrayList<String> matchWithoutTopics = new ArrayList<String>();
			for (BrokerSubscription<T> subscription : subscriptions) {
				if (!subscription.hasTopics()) {
					matchWithoutTopics.add(subscription.getSubscriber()
							.getIdentifier());
				}
			}
			return matchWithoutTopics;
		}
	}

}
