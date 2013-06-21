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

public class ChannelBasedFiltering<T extends IStreamObject<?>> extends
		AbstractFiltering<T> {

	Map<String, List<String>> matches = new HashMap<String, List<String>>();

	@Override
	public void reinitializeFilter(
			Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		matches.clear();
		for (BrokerSubscription<T> subscription : subscriptions) {
			for (BrokerAdvertisements advertisement : advertisements) {
				boolean addSubscriberToPublisher = true;
				// Wenn Advertisement mehr Topics hat, ist der Subscriber nicht
				// interessiert
				if (advertisement.getTopics().size() > subscription.getTopics()
						.size() || subscription.getTopics().size() == 0) {
					addSubscriberToPublisher = false;
				}
				for (Topic topic : advertisement.getTopics()) {
					// Ist diese Topic in Advertisement nicht enthalten, dann
					// Subscriber und Publisher nicht verbinden
					if (!hasSubscriptionTopic(topic, subscription)) {
						addSubscriberToPublisher = false;
						break;
					}
				}

				if (addSubscriberToPublisher) {
					// Add to Routing Table
					if (matches.containsKey(advertisement.getPublisher()
							.getIdentifier())) {
						// If publisher already has assigned subscribers
						if (!matches.get(
								advertisement.getPublisher().getIdentifier())
								.contains(
										subscription.getSubscriber()
												.getIdentifier())) {
							matches.get(
									advertisement.getPublisher()
											.getIdentifier()).add(
									subscription.getSubscriber()
											.getIdentifier());
						}
					} else {
						// If publisher has no assigned subscribers
						List<String> matchedSubscriber = new ArrayList<String>();
						matchedSubscriber.add(subscription.getSubscriber()
								.getIdentifier());
						matches.put(advertisement.getPublisher()
								.getIdentifier(), matchedSubscriber);
					}
				}
			}
		}
		setReinitializationMode(false);
	}

	private boolean hasSubscriptionTopic(Topic topic,
			BrokerSubscription<T> subscription) {
		for (Topic subscriptionTopic : subscription.getTopics()) {
			if (subscriptionTopic.equals(topic)) {
				return true;
			}
		}
		return false;
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
