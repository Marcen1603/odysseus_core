package de.uniol.inf.is.odysseus.incubation.graph;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;

public interface IGraphBuilder {
	
	public IGraphDataStructure<IMetaAttribute> getGraphDataStructure();
}
