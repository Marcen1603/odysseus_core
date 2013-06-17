package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T>{

	
	public SimpleBroker(String name) {
		super(name);
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber) {
		
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			SubscribePO<T> subscriber) {

	}

	@Override
	public void routeToSubscribers(IStreamObject<?> object) {

	}
}
