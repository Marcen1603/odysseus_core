package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBrokerService {

	/**
	 * Initializes the Broker or the brokernetwork if isNetworkDistributed is set
	 * 
	 * @param isNetworkDistributed
	 */
	void init(boolean isNetworkDistributed);

	/**
	 * subscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void subscribe(String brokername, List<IPredicate<?>> predicates, SubscribePO<?> subscriber);

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void unsubscribe(String brokername, List<IPredicate<?>> predicates, SubscribePO<?> subscriber);
	
	/**
	 * a producer / publisher can advertise the transfered content (optional)
	 * 
	 * @param predicates
	 * @param publisher
	 */
	void advertise(List<IPredicate<?>> predicates, PublishPO publisher);
	
	/**
	 * a producer / publisher can unadvertise the transfered content (optional)
	 * only needed if an advertise was set before
	 * 
	 * @param predicates
	 * @param publisher
	 */
	void unadvertise(List<IPredicate<?>> predicates, PublishPO publisher);
	
	/**
	 * the publisher uses the transfer method to publish the content to the brokernetwork
	 * and to all subscribers
	 */
	void transfer();
}
