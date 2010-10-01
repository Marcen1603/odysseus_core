package de.uniol.inf.is.odysseus.metadata;

import de.uniol.inf.is.odysseus.IClone;

/**
 * @author Jonas Jacobi
 */
public interface IMetadataUpdater<M extends IClone, T extends IMetaAttributeContainer<? extends M>> {
	
	public void updateMetadata(T inElem);
	public String getName();
	
}
