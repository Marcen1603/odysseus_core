package de.uniol.inf.is.odysseus.metadata;

import de.uniol.inf.is.odysseus.IClone;

abstract public class AbstractMetadataUpdater<M extends IClone, T extends IMetaAttributeContainer<? extends M>> implements IMetadataUpdater<M, T> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

}
