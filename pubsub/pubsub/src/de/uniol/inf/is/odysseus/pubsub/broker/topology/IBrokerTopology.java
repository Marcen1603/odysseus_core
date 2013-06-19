package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBrokerTopology<T extends IStreamObject<?>> {
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	<E extends IStreamObject<?>> IBrokerTopology<?> getInstance(String name);
	
	/**
	 * 
	 * @return
	 */
	String getType();
	
	/**
	 * 
	 * @return
	 */
	String getDomain();
	
	/**
	 * subscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscribePO
	 */
	void subscribe(List<IPredicate<? super T>> predicates, List<Topic> topics , String brokerName, SubscribePO<T> subscribePO);

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	void unsubscribe(List<IPredicate<? super T>> predicates, List<Topic> topics , String brokerName, SubscribePO<T> subscriber);
	
	
	/**
	 * the publisher uses the transfer method to publish the content to the brokernetwork
	 * and to all subscribers
	 */
	void transfer(T object);
	
	/**
	 * Finds the best Broker
	 * @return
	 */
	List<IBroker<T>> getAdressedBrokers();

	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void advertise(List<Topic> topics, PublishPO<T> publisher);

	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void unadvertise(List<Topic> topics, PublishPO<T> publisher);
}
