package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public abstract class AbstractTopicBasedFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T> {

	protected abstract boolean hasSubscriptionTopic(Topic topic,
			BrokerSubscription<T> subscription);
	
	Map<String, List<String>> matches = new HashMap<String, List<String>>();

	@Override
	public void reinitializeFilter(
			Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		matches.clear();
		for (BrokerSubscription<T> subscription : subscriptions) {
			for (BrokerAdvertisements advertisement : advertisements) {
				boolean addSubscriberToPublisher = true;
				
				// if advertisement is more concrete, subscriber is not interested
				if (advertisement.getTopics().size() > subscription.getTopics()
						.size() || subscription.getTopics().size() == 0) {
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

				// Match publisher and subscriber if topis match
				if (addSubscriberToPublisher) {

					if (matches.containsKey(advertisement.getPublisherUid())) {
						// If publisher already has assigned subscribers
						if (!matches.get(
								advertisement.getPublisherUid())
								.contains(
										subscription.getSubscriber()
												.getIdentifier())) {
							matches.get(
									advertisement.getPublisherUid()).add(
									subscription.getSubscriber()
											.getIdentifier());
						}
					} else {
						// If publisher has no assigned subscribers
						List<String> matchedSubscriber = new ArrayList<String>();
						matchedSubscriber.add(subscription.getSubscriber()
								.getIdentifier());
						matches.put(advertisement.getPublisherUid(), matchedSubscriber);
					}
				}
			}
		}
		setReinitializationMode(false);
	}

	@Override
	public List<String> filter(T object, PublishPO<T> publisher) {
		if (matches.containsKey(publisher.getIdentifier())) {
			return matches.get(publisher
					.getIdentifier());
		}
		return new ArrayList<String>();
	}

}
