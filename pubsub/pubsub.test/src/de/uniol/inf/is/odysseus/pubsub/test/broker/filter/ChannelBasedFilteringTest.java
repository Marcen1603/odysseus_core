package de.uniol.inf.is.odysseus.pubsub.test.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.ChannelBasedFiltering;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * 
 * @author ChrisToenjesDeye
 *
 * @param <T>
 */
public class ChannelBasedFilteringTest<T extends IStreamObject<?>>{

	// TODO Add more test cases
	
	private ChannelBasedFiltering<T> filter;
	
	@BeforeMethod
	public void startUp(){
		filter = new ChannelBasedFiltering<T>();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void equalFilterTest(){
		List<String> topics = new ArrayList<String>();
		topics.add("news");
		List<IPredicate<? super T>> predicates = new ArrayList<IPredicate<? super T>>();
		
		Collection<BrokerSubscription<T>> subscriptions = new ArrayList<BrokerSubscription<T>>();
		SubscribePO<T> subscriber = new SubscribePO<T>(predicates, "broker0815", null, topics, "default");
		subscriptions.add(new BrokerSubscription<T>(subscriber));
		
		Collection<BrokerAdvertisements> advertisements = new ArrayList<BrokerAdvertisements>();
		PublishPO<T> publisher = new PublishPO<>("topology", "domain", topics, "");
		advertisements.add(new BrokerAdvertisements(publisher.getIdentifier(), publisher.getTopics()));
		
		filter.reinitializeFilter(subscriptions, advertisements);
		List<String> matchedSubscriber = filter.filter((T) new KeyValueObject<>(), publisher);
		Assert.assertTrue(matchedSubscriber.contains(subscriber.getIdentifier()));
	}

}
