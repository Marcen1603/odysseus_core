package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * @author Jonas Jacobi
 */
public interface IMetadataUpdater<M extends IClone, T extends IMetaAttributeContainer<? extends M>> {
	
	public void updateMetadata(T inElem);
	public String getName();
	
}
