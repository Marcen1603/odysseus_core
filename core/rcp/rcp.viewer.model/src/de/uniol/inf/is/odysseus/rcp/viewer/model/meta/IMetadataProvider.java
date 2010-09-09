package de.uniol.inf.is.odysseus.rcp.viewer.model.meta;

import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public interface IMetadataProvider<M> {

	public Collection<String> getList();
	public M createMetadata( String type, INodeModel<?> nodeModel );
	
}
