package de.uniol.inf.is.odysseus.rcp.viewer.view.graph;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public interface IOdysseusNodeView extends INodeView<IPhysicalOperator> {

	public void connect();
	public void disconnect();
	public IOdysseusNodeModel getModelNode();
	
}
