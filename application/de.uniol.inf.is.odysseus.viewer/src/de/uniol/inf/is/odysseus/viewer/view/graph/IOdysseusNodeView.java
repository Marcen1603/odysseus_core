package de.uniol.inf.is.odysseus.viewer.view.graph;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.model.graph.IOdysseusNodeModel;

public interface IOdysseusNodeView extends INodeView<IPhysicalOperator> {

	public void connect();
	public void disconnect();
	public IOdysseusNodeModel getModelNode();
	
}
