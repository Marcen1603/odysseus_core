package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBrokerTopology<T extends IStreamObject<?>> {
	
	String getName();
	
	/**
	 * subscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscribePO
	 */
	void subscribe(List<IPredicate<? super T>> predicates, SubscribePO<T> subscribePO, String brokerName);

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void unsubscribe(List<IPredicate<? super T>> predicates, SubscribePO<T> subscriber, String brokerName);
	
	/**
	 * a producer / publisher can advertise the transfered content (optional)
	 * 
	 * @param predicates
	 * @param publisher
	 */
	void advertise(List<IPredicate<? super T>> predicates, PublishPO<T> publisher);
	
	/**
	 * a producer / publisher can unadvertise the transfered content (optional)
	 * only needed if an advertise was set before
	 * 
	 * @param predicates
	 * @param publisher
	 */
	void unadvertise(List<IPredicate<? super T>> predicates, PublishPO<T> publisher);
	
	/**
	 * the publisher uses the transfer method to publish the content to the brokernetwork
	 * and to all subscribers
	 */
	void transfer(IStreamObject<?> object);
	
	/**
	 * Finds the best Broker
	 * @return
	 */
	List<IBroker<T>> getAdressedBrokers();

}
