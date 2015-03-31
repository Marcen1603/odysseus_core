package de.uniol.inf.is.odysseus.core.server.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class MetadataInitializerAdapter<M extends IMetaAttribute, T extends IStreamObject<M>>
		implements IMetadataInitializer<M, T> {

	Class<M> type;
	private List<IMetadataUpdater<M, T>> metadataUpdaters = new CopyOnWriteArrayList<>();

	@Override
	public void setMetadataType(Class<M> type) {
		this.type = type;
	}

	@Override
	public M getMetadataInstance() throws InstantiationException,
			IllegalAccessException {
		return type.newInstance();
	}

	@Override
	public void addMetadataUpdater(IMetadataUpdater<M, T> metadataUpdater) {
		this.metadataUpdaters.add(metadataUpdater);
	}

	@Override
	public void updateMetadata(T object) {
		for (IMetadataUpdater<M, T> metadataUpdater : metadataUpdaters) {
			metadataUpdater.updateMetadata(object);
		}
	}

}
