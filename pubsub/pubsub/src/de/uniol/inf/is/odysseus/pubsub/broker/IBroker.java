package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBroker<T extends IStreamObject<?>> {

	/**
	 * Returns the Name of the Broker
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @param predicates
	 * @param subscriber
	 */
	void setSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber);
	
	/**
	 * 
	 * @param predicates
	 * @param subscriber
	 */
	void removeSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber);
	
	
	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void setAdvertisement(List<Topic> topics,
			PublishPO<T> publisher);
	
	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void removeAdvertisement(List<Topic> topics,
			PublishPO<T> publisher);
	
	/**
	 * 
	 * @param object
	 */
	void routeToSubscribers(T object);
	
	/**
	 * 
	 * @return
	 */
	boolean hasSubscriptions();
}
