package de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;

public class OdysseusGraphView extends DefaultGraphView<IPhysicalOperator> implements IOdysseusGraphView {

	public OdysseusGraphView(IOdysseusGraphModel data) {
		super(data);
	}

	@Override
	public IOdysseusGraphModel getModelGraph() {
		return (IOdysseusGraphModel)super.getModelGraph();
	}
}
