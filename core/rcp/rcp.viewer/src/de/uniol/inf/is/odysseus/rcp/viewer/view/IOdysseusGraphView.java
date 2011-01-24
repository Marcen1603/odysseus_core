package de.uniol.inf.is.odysseus.rcp.viewer.view;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;

public interface IOdysseusGraphView extends IGraphView<IPhysicalOperator>{
	@Override
	public IOdysseusGraphModel getModelGraph();
}
