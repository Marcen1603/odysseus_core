package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public interface IRoutingBroker <T extends IStreamObject<?>> extends IBroker<T>{
	
	String getType();
	
	IRoutingBroker<T> getInstance(String name, String domain);
	
	void route(T object, PublishPO<T> publisher, String sourceIdentifier);

	List<IRoutingBroker<T>> getConnectedBrokers();

	void distributeAdvertisement(List<Topic> topics, String publisherUid, String sourceIdentifier);
	
	void removeDistributedAdvertisement(List<Topic> topics, String publisherUid, String sourceIdentifier);

	String getIdentifier();
}
