package de.uniol.inf.is.odysseus.pubsub.broker;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

enum Filtertype{
	content,
	hierarchical,
	channel
}

public abstract class AbstractBroker<T extends IStreamObject<?>> implements IBroker<T>{
	private String name;
	private String domain;
	
	public AbstractBroker(String name, String domain){
		this.name = name;
		this.domain = domain;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDomain() {
		return domain;
	}
}
