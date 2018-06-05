package de.uniol.inf.is.odysseus.rcp.viewer.view.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;

public class OdysseusConnectionView extends DefaultConnectionView<IPhysicalOperator> {

	public OdysseusConnectionView(IConnectionModel<IPhysicalOperator> data, INodeView<IPhysicalOperator> start, INodeView<IPhysicalOperator> end) {
		super(data, start, end);
	}

	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof OdysseusConnectionView )) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		
		OdysseusConnectionView other = (OdysseusConnectionView)obj;
		return other.getViewedEndNode().equals(getViewedEndNode()) && other.getViewedStartNode().equals(getViewedStartNode()) && other.getModelConnection().equals(getModelConnection());
	}
}
