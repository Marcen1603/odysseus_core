package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;

public interface IRoutingBroker <T extends IStreamObject<?>> extends IBroker<T>{
	
	String getType();
	
	IRoutingBroker<T> getInstance(String name, String domain);
	
	void route();

}
