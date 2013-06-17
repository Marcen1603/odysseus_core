package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
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
	void setSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber);
	
	/**
	 * 
	 * @param predicates
	 * @param subscriber
	 */
	void removeSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber);
	
	/**
	 * 
	 * @param object
	 */
	void routeToSubscribers(IStreamObject<?> object);
	
	/**
	 * 
	 * @return
	 */
	boolean hasSubscriptions();
}
