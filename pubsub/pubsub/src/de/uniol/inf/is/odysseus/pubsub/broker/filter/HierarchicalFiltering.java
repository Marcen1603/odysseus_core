package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class HierarchicalFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T>{

	@Override
	public void reinitializeFilter(List<BrokerSubscription<T>> subscriptions,
			List<BrokerAdvertisements> advertisements) {
		
		setReinitializationMode(false);
	}

	@Override
	public void filter(T object, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

}
