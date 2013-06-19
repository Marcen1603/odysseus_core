package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.ArrayList;
import java.util.List;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T>{

	private List<BrokerSubscription<T>> subscriptions;
	private List<BrokerAdvertisements> advertisements;
	private boolean isAnyTopicHierarchical;
	
	public SimpleBroker(String name) {
		super(name);
		subscriptions = new ArrayList<BrokerSubscription<T>>();
		advertisements = new ArrayList<BrokerAdvertisements>();
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber) {
		subscriptions.add(new BrokerSubscription<T>(subscriber, predicates, topics));
		refreshIsAnyTopicHierarchical();
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber) {
		// TODO remove Subscription
		refreshIsAnyTopicHierarchical();
	}

	@Override
	public void setAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		advertisements.add(new BrokerAdvertisements(publisher, topics));
		refreshIsAnyTopicHierarchical();
	}

	@Override
	public void removeAdvertisement(List<Topic> topics,
			PublishPO<T> publisher) {
		// TODO remove Advertisement
		refreshIsAnyTopicHierarchical();
	}

	@Override
	public void routeToSubscribers(T object) {
		
		for (BrokerSubscription<T> subscription : subscriptions) {
			if (subscription.hasTopics() && doesAnyBrokerAdvertiseTopics()){
				if (!isAnyTopicHierarchical){
					// Channel based filtering
					
				} else {
					// Hierarchical Filtering
					
				}
			} else if (subscription.hasPredicates()){
				// Content based filtering
				
			}
			
			
			// TODO Marco um Rat fragen !
			//subscription.getSubscriber().process_next(object, 0);
		}
	}

	private boolean doesAnyBrokerAdvertiseTopics() {
		if (!advertisements.isEmpty()){
			return true;
		}
		return false;
	}
	
	private boolean refreshIsAnyTopicHierarchical(){
		for (BrokerSubscription<T> subscription : subscriptions) {
			for (Topic topic : subscription.getTopics()) {
				if (topic.isHierarchical()){
					return true;
				}
			}
		}
		for (BrokerAdvertisements advertisement : advertisements) {
			for (Topic topic : advertisement.getTopics()) {
				if (topic.isHierarchical()){
					return true;
				}
			}
		}
		return false;
	}
}
