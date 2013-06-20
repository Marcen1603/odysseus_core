package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private List<BrokerSubscription<T>> subscriptions;
	private List<BrokerAdvertisements> advertisements;

	private boolean isAnyTopicHierarchical = false;
	private boolean hasAnySubscriptionTopics = false;
	private boolean hasAnySubscriptionPredicates = false;
	
	private Map<Filtertype, IFiltering<T>> filters = new HashMap<Filtertype, IFiltering<T>>();
	
	

	public SimpleBroker(String name) {
		super(name);
		subscriptions = new ArrayList<BrokerSubscription<T>>();
		advertisements = new ArrayList<BrokerAdvertisements>();
		
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
		subscriptions.add(new BrokerSubscription<T>(subscriber, predicates,
				topics));
		refreshInternalStatus();
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		// TODO remove Subscription
		refreshInternalStatus();
	}

	@Override
	public void setAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		advertisements.add(new BrokerAdvertisements(publisher, topics));
		refreshInternalStatus();
	}

	@Override
	public void removeAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		// TODO remove Advertisement
		refreshInternalStatus();
	}

	@Override
	public void routeToSubscribers(T object, PublishPO<T> publisher) {
		// TODO Object soll an den Subscriber nur einmal weitergeleitet werden.
		
		if (hasAnySubscriptionTopics && doesAnyPublisherAdvertiseTopics()) {
			if (!isAnyTopicHierarchical) {
				// Channel based filtering
				IFiltering<T> filter = filters.get(Filtertype.channel);
				if (filter.needsReinitialization()){
					filter.reinitializeFilter(subscriptions, advertisements);
				}
				filter.filter(object, publisher);
			} else {
				// Hierarchical Filtering
				IFiltering<T> filter = filters.get(Filtertype.hierarchical);
				if (filter.needsReinitialization()){
					filter.reinitializeFilter(subscriptions, advertisements);
				}
				filter.filter(object, publisher);
			}
		}
		if (hasAnySubscriptionPredicates) {
			// Content based filtering
			IFiltering<T> filter = filters.get(Filtertype.content);
			if (filter.needsReinitialization()){
				filter.reinitializeFilter(subscriptions, advertisements);
			}
			filter.filter(object, publisher);
		}
	}

	private boolean doesAnyPublisherAdvertiseTopics() {
		if (!advertisements.isEmpty()) {
			return true;
		}
		return false;
	}

	private void refreshInternalStatus() {
		isAnyTopicHierarchical = false;
		hasAnySubscriptionTopics = false;
		hasAnySubscriptionPredicates = false;

		for (BrokerSubscription<T> subscription : subscriptions) {
			if (subscription.hasTopics()){
				hasAnySubscriptionTopics = true;
			}
			if (subscription.hasPredicates()){
				hasAnySubscriptionPredicates = true;
			}
			for (Topic topic : subscription.getTopics()) {
				if (topic.isHierarchical()) {
					isAnyTopicHierarchical =  true;
				}
			}
		}
		for (BrokerAdvertisements advertisement : advertisements) {
			for (Topic topic : advertisement.getTopics()) {
				if (topic.isHierarchical()) {
					isAnyTopicHierarchical =  true;
				}
			}
		}
		
		// All filters needs reinitialization
		filters.get(Filtertype.channel).setReinitializationMode(true);
		filters.get(Filtertype.hierarchical).setReinitializationMode(true);
		filters.get(Filtertype.content).setReinitializationMode(true);
	}
}
