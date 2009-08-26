package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IClone;

public interface IMetadataFactory<M extends IClone, In> {
	
	public M createMetadata(In inElem);
	
	public M createMetadata();

}
