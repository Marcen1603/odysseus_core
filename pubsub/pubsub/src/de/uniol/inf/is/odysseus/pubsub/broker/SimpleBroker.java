package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T>{

	public SimpleBroker(String name) {
		super(name);
	}

	

	private void setSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<?> subscriber) {
		// TODO Auto-generated method stub
		
	}

	private void removeSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<?> subscriber) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void subscribe(List<IPredicate<? super T>> predicates, SubscribePO<T> subscriber) {
		this.setSubscription(predicates, subscriber);
	}

	@Override
	public void unsubscribe(List<IPredicate<? super T>> predicates, SubscribePO<T> subscriber) {
		this.removeSubscription(predicates, subscriber);
	}

	@Override
	public void advertise(List<IPredicate<? super T>> predicates, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unadvertise(List<IPredicate<? super T>> predicates, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transfer(IStreamObject<?> object) {
		// TODO Auto-generated method stub
		// send object to all Subscribers
	}
}
