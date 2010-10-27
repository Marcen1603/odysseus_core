package de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModelChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class OdysseusNodeView extends DefaultNodeView<IPhysicalOperator> implements IOdysseusNodeView, INodeModelChangeListener< IPhysicalOperator > {
	
	public OdysseusNodeView( IOdysseusNodeModel data ) {
		super( data );
	}

	@Override
	public void nodeModelChanged( INodeModel< IPhysicalOperator > sender ) {}

	@Override
	public IOdysseusNodeModel getModelNode() {
		return (IOdysseusNodeModel)super.getModelNode();
	}

	@Override
	public void connect() {
		if( getModelNode() != null ) 
			getModelNode().addNodeModelChangeListener( this );
	}
	
	@Override
	public void disconnect() {
		if( getModelNode() != null ) 
			getModelNode().removeNodeModelChangeListener( this );
	}

}
