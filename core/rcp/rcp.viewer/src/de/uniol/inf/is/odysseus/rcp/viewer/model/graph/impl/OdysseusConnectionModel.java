package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public class OdysseusConnectionModel extends DefaultConnectionModel<IPhysicalOperator> {

	private final PhysicalSubscription<?> subscription;
	
	public OdysseusConnectionModel(INodeModel<IPhysicalOperator> startNode, INodeModel<IPhysicalOperator> endNode, PhysicalSubscription<?> subscription) {
		super(startNode, endNode);
		this.subscription = subscription;
	}

	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof OdysseusConnectionModel )) {
			return false;
		}
		if( obj == this ) {
			return true;
		}

		OdysseusConnectionModel other = (OdysseusConnectionModel)obj;
		return other.getStartNode().equals(getStartNode()) && other.getEndNode().equals(getEndNode()) && other.subscription.equals(subscription);
	}
}
