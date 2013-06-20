package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public abstract class AbstractFiltering<T extends IStreamObject<?>> implements IFiltering<T>{

	private boolean needsReinitialization;

	@Override
	public void setReinitializationMode(boolean mode) {
		this.needsReinitialization = mode;
	}
	
	@Override
	public boolean needsReinitialization() {
		return needsReinitialization;
	}
	

	
}
