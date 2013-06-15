package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class Broker {

	private String name;

	public Broker(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSubscription(List<IPredicate<?>> predicates,
			SubscribePO<?> subscriber) {
		// TODO Auto-generated method stub
		
	}

	public void removeSubscription(List<IPredicate<?>> predicates,
			SubscribePO<?> subscriber) {
		// TODO Auto-generated method stub
		
	}
}
