package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public abstract class AbstractBrokerTopology<T extends IStreamObject<?>> implements IBrokerTopology<T>{
	
	abstract IBroker<T> getBrokerByName(String name);
	
	private String domainName;
	
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber, String brokerName) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, subscriber);			
	}

	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber, String brokerName) {
		IBroker<T> broker = getBrokerByName(brokerName);
		broker.setSubscription(predicates, subscriber);	
	}

	
	
	@Override
	public void transfer(IStreamObject<?> object) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.routeToSubscribers(object);			
		}
	}
	
	@Override
	public void advertise(List<Topic> predicates,
			PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.setAdvertisement(predicates, publisher);			
		}
	}

	@Override
	public void unadvertise(List<Topic> predicates,
			PublishPO<T> publisher) {
		List<IBroker<T>> brokers = getAdressedBrokers();
		for (IBroker<T> broker : brokers) {
			broker.removeAdvertisement(predicates, publisher);			
		}
	}
	
	
	public String getDomain() {
		return domainName;
	}
	
	public void setDomain(String domain){
		this.domainName = domain;
	}
	

}
