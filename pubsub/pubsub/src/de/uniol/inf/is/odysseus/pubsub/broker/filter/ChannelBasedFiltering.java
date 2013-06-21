package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class ChannelBasedFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T>{
	
	Map<String, List<SubscribePO<T>>> matches = new HashMap<String, List<SubscribePO<T>>>();
	
	@Override
	public void reinitializeFilter(List<BrokerSubscription<T>> subscriptions,
			List<BrokerAdvertisements> advertisements) {
		matches.clear();
		for (BrokerSubscription<T> subscription : subscriptions) {
			for (Topic topic : subscription.getTopics()) {
				matchPublisherAndSubscriber(advertisements, topic, subscription);
			}
		}
		setReinitializationMode(false);
	}
	
	private void matchPublisherAndSubscriber(List<BrokerAdvertisements> advertisements, Topic subscriberTopic, BrokerSubscription<T> subscription){
		for (BrokerAdvertisements advertisement : advertisements) {
			for (Topic topic : advertisement.getTopics()) {
				if (topic.equals(subscriberTopic)){
					// Add to Routing Table
					if (matches.containsKey(advertisement.getPublisher().getIdentifier())){
						// If publisher already has assigned subscribers
						matches.get(advertisement.getPublisher().getIdentifier()).add(subscription.getSubscriber());
					} else {
						// If publisher has no assigned subscribers
						List<SubscribePO<T>> matchedSubscriber = new ArrayList<SubscribePO<T>>();
						matchedSubscriber.add(subscription.getSubscriber());
						matches.put(advertisement.getPublisher().getIdentifier(), matchedSubscriber);						
					}
				}
			}
		}
		
	}

	@Override
	public void filter(T object, PublishPO<T> publisher) {
		for (SubscribePO<T> subscriber : matches.get(publisher.getIdentifier())) {
			subscriber.receive(object);
		}
	}

}
