package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IClone;

abstract public class AbstractMetadataUpdater<M extends IClone, T extends IMetaAttributeContainer<? extends M>> implements IMetadataUpdater<M, T> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

}
