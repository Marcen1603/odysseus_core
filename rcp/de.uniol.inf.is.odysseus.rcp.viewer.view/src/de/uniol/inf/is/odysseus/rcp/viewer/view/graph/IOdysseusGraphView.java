package de.uniol.inf.is.odysseus.rcp.viewer.view.graph;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;

public interface IOdysseusGraphView extends IGraphView<IPhysicalOperator>{
	public IOdysseusGraphModel getModelGraph();
}
