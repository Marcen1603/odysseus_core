package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.routing.IRoutingBroker;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public abstract class AbstractRoutingTopology<T extends IStreamObject<?>>
		extends AbstractTopology<T> {

	abstract IRoutingBroker<T> getBrokerByName(String name);

	abstract IRoutingBroker<T> getBestBroker(String publisherUid);
	
	private String routingType;

	/**
	 * subscribes a Subscriber with given Filterpredicates on the given broker
	 * 
	 * @param brokername
	 * @param list
	 * @param subscriber
	 */
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IRoutingBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, topics, subscriber);
	}

	/**
	 * unsubscribes a Subscriber with given Filterpredicates on a given broker
	 * 
	 * @param brokername
	 * @param predicates
	 * @param subscriber
	 */
	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IRoutingBroker<T> broker = getBrokerByName(brokerName);
		broker.removeSubscription(predicates, topics, subscriber);
	}
	
	@Override
	public void transfer(T object, PublishPO<T> publisher) {
		getBestBroker(publisher.getIdentifier()).route(object, publisher, "");
	}
	
	@Override
	public boolean needsRouting(){
		return true;
	}
	
	@Override
	public void setRoutingType(String routingType){
		this.routingType = routingType;
	}
	
	public String getRoutingType(){
		return routingType;
	}

}
