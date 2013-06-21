package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public interface IFiltering<T extends IStreamObject<?>> {

	/**
	 * Initialize and optimize Filter for better performance
	 * @param subscriptions
	 * @param advertisements
	 */
	void reinitializeFilter(Collection<BrokerSubscription<T>> subscriptions, Collection<BrokerAdvertisements> advertisements);
	
	/**
	 * Send Object to subscribers
	 * @param object
	 * @param publisher
	 */
	List<String> filter(T object, PublishPO<T> publisher);

	/**
	 * 
	 */
	void setReinitializationMode(boolean mode);

	/**
	 * 
	 * @return
	 */
	boolean needsReinitialization();
}
