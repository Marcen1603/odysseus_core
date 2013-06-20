package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public abstract class AbstractBrokerTopology<T extends IStreamObject<?>>
		implements IBrokerTopology<T> {

	abstract IBroker<T> getBrokerByName(String name);

	private String domainName;
	
	private int numberOfAgents = 0;

	@Override
	public void incrementNumberOfAgents(){
		numberOfAgents++;
	}
	
	@Override
	public void decrementNumberOfAgents(){
		numberOfAgents--;
	}
	
	public boolean hasAnyAgents(){
		if (numberOfAgents == 0){
			return false;
		}
		return true;
	}
	
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, topics, subscriber);
	}

	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			List<Topic> topics, String brokerName, SubscribePO<T> subscriber) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.removeSubscription(predicates, topics, subscriber);
	}

	@Override
	public void transfer(T object, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.routeToSubscribers(object, publisher);
		}
	}

	@Override
	public void advertise(List<Topic> topics, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.setAdvertisement(topics, publisher);
		}
	}

	@Override
	public void unadvertise(List<Topic> topics, PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.removeAdvertisement(topics, publisher);
		}
	}

	@Override
	public String getDomain() {
		return domainName;
	}

	public void setDomain(String domain) {
		this.domainName = domain;
	}

}
