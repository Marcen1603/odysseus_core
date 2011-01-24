package de.uniol.inf.is.odysseus.rcp.viewer.view;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public interface IOdysseusNodeView extends INodeView<IPhysicalOperator> {

	public void connect();
	public void disconnect();
	@Override
	public IOdysseusNodeModel getModelNode();
	
}
