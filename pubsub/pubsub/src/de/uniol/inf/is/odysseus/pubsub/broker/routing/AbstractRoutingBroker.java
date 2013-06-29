package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.UUID;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.AbstractBroker;

public abstract class AbstractRoutingBroker<T extends IStreamObject<?>> extends AbstractBroker<T> implements IRoutingBroker<T>{

	protected String identifier;
	
	public AbstractRoutingBroker(String name, String domain) {
		super(name, domain);
		identifier = UUID.randomUUID().toString();
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

}
