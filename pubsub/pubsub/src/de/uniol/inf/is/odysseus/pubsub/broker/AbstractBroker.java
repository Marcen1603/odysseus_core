package de.uniol.inf.is.odysseus.pubsub.broker;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public abstract class AbstractBroker<T extends IStreamObject<?>> implements IBroker<T>{
	private String name;
	
	
	public AbstractBroker(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
