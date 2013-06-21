package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class ContentBasedFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T>{

	private List<BrokerSubscription<T>> subscriptions;
	
	@Override
	public void reinitializeFilter(List<BrokerSubscription<T>> subscriptions,
			List<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.subscriptions = subscriptions;
		setReinitializationMode(false);
	}

	@Override
	public void filter(T object, PublishPO<T> publisher) {
		// TODO naiver Ansatz
		for (BrokerSubscription<T> subscription : subscriptions) {
			for (IPredicate<? super T> pred : subscription.getPredicates()) {
				if (pred.evaluate(object)){
					subscription.getSubscriber().receive(object);
				}
			}
		}
	}

}
