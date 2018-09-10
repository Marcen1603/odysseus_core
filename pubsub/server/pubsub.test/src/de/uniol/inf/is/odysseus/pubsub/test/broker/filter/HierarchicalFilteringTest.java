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

package de.uniol.inf.is.odysseus.pubsub.test.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.HierarchicalFiltering;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * Tests for hierarchical Filtering
 *
 * @author ChrisToenjesDeye
 *
 */
public class HierarchicalFilteringTest<T extends IStreamObject<?>> {

	private HierarchicalFiltering<T> filter;
	private SubscribePO<T> subscriber;
	private PublishPO<T> publisher;

	@BeforeMethod
	public void startUp() {
		filter = new HierarchicalFiltering<T>();
	}

	@Test
	public void equalFilterTest() {
		List<String> topics = new ArrayList<String>();
		topics.add("news.wirtschaft");
		topics.add("news.politik");
		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();

		List<String> matchedSubscriber = processFiltering(topics,
				topics, predicates);
		Assert.assertTrue(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

	@Test
	public void differentFiltersMatchTest() {
		List<String> subscribertopics = new ArrayList<String>();
		subscribertopics.add("news");

		List<String> publisherTopics = new ArrayList<String>();
		publisherTopics.add("news.wirtschaft");
		publisherTopics.add("news.politik");

		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();

		List<String> matchedSubscriber = processFiltering(subscribertopics,
				publisherTopics, predicates);
		Assert.assertTrue(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

	@Test
	public void differentFiltersNotMatchTest() {
		List<String> subscribertopics = new ArrayList<String>();
		subscribertopics.add("news.wirtschaft");
		subscribertopics.add("news.politik");

		List<String> publisherTopics = new ArrayList<String>();
		publisherTopics.add("news");


		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();

		List<String> matchedSubscriber = processFiltering(subscribertopics,
				publisherTopics, predicates);
		Assert.assertFalse(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

	@Test
	public void publisherHasNoTopicsTest() {
		List<String> subscribertopics = new ArrayList<String>();
		subscribertopics.add("news.wirtschaft");
		subscribertopics.add("news.politik");

		List<String> publisherTopics = new ArrayList<String>();

		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();

		List<String> matchedSubscriber = processFiltering(subscribertopics,
				publisherTopics, predicates);
		Assert.assertTrue(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

	@Test
	public void subscriberHasNoTopicsTest() {
		List<String> subscribertopics = new ArrayList<String>();

		List<String> publisherTopics = new ArrayList<String>();
		publisherTopics.add("news.wirtschaft");
		publisherTopics.add("news.politik");

		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();

		List<String> matchedSubscriber = processFiltering(subscribertopics,
				publisherTopics, predicates);
		Assert.assertTrue(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

	@SuppressWarnings("unchecked")
	private List<String> processFiltering(List<String> subscribertopics,
			List<String> publisherTopics, List<IPredicate<? super T>> predicates) {
		Collection<BrokerSubscription<T>> subscriptions = new ArrayList<BrokerSubscription<T>>();
		subscriber = new SubscribePO<T>(predicates,
				true,  subscribertopics, "default");
		subscriptions.add(new BrokerSubscription<T>(subscriber));

		Collection<BrokerAdvertisements> advertisements = new ArrayList<BrokerAdvertisements>();
		publisher = new PublishPO<>("topology", "domain", publisherTopics, "");
		advertisements.add(new BrokerAdvertisements(publisher.getIdentifier(), publisher.getTopics()));

		filter.reinitializeFilter(subscriptions, advertisements);
		List<String> matchedSubscriber = filter.filter(
				(T) KeyValueObject.createInstance(), publisher.getIdentifier());
		return matchedSubscriber;
	}

}
