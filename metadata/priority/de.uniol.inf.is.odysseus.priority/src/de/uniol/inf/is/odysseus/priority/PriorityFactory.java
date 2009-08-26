package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;

public class PriorityFactory implements IMetadataFactory<IPriority, Object> {

	@Override
	public IPriority createMetadata(Object inElem) {
		return new Priority();
	}

	@Override
	public IPriority createMetadata() {
		return new Priority();
	}
	
}
