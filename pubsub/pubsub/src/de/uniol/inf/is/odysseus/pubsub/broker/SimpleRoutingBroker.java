package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class SimpleRoutingBroker <T extends IStreamObject<?>> extends AbstractBroker<T>{

	//private List<SimpleRoutingBroker<T>> connectedBrokers = new ArrayList<SimpleRoutingBroker<T>>();
	
	
	public SimpleRoutingBroker(String name) {
		super(name);
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAdvertisement(List<Topic> topics, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void routeToSubscribers(T object, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSubscriptions() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
