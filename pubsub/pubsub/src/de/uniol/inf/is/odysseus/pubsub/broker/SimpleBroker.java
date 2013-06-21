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

		ArrayList<String> matchedSubscriber = new ArrayList<String>();
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics()) {
			if (!isAnyTopicHierarchical) {
				// Channel based filtering
				IFiltering<T> filter = filters.get(Filtertype.channel);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
				}
				matchedSubscriber.addAll(filter.filter(object, publisher));
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				if (filter.needsReinitialization()) {
					filter.reinitializeFilter(subscriptions.values(),
							advertisements.values());
				}
				matchedSubscriber.addAll(filter.filter(object, publisher));
			}
		}
		if (hasAnySubscriptionPredicates) {
			// Content based filtering
			IFiltering<T> filter = filters.get(Filtertype.content);
			if (filter.needsReinitialization()) {
				filter.reinitializeFilter(subscriptions.values(),
						advertisements.values());
			}
			matchedSubscriber.addAll(filter.filter(object, publisher));
		}

		// TODO Object soll an den Subscriber nur einmal weitergeleitet werden.
		for (String subscriberId : matchedSubscriber) {
			subscriptions.get(subscriberId).getSubscriber().receive(object);
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
