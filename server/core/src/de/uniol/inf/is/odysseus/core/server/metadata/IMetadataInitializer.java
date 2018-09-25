package de.uniol.inf.is.odysseus.core.server.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public interface IMetadataInitializer<M extends IMetaAttribute, T extends IStreamObject<M> > {
	void setMetadataType(IMetaAttribute type);
	M getMetadataInstance() throws InstantiationException, IllegalAccessException;
	void addMetadataUpdater(IMetadataUpdater<M, T> mFac);
	void updateMetadata(T object);
}
