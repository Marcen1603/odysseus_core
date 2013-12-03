package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public class OdysseusConnectionModel extends DefaultConnectionModel<IPhysicalOperator> {

	private final PhysicalSubscription<?> sourceSubscription;
	private final PhysicalSubscription<?> sinkSubscription;
	
	public OdysseusConnectionModel(INodeModel<IPhysicalOperator> startNode, INodeModel<IPhysicalOperator> endNode) {
		super(startNode, endNode);
		
		this.sourceSubscription = determineSourceSubscription();
		this.sinkSubscription = determineSinkSubscription();
	}

	private PhysicalSubscription<?> determineSourceSubscription() {
		ISource<?> startOperator = (ISource<?>)getStartNode().getContent();
		ISink<?> endOperator = (ISink<?>)getEndNode().getContent();
		
		for( PhysicalSubscription<?> sub : endOperator.getSubscribedToSource()) {
			if( sub.getTarget() == startOperator ) {
				return sub;
			}
		}
		
		// normally, we are not here now
		throw new RuntimeException("Could not find the subscription from source to sink");
	}

	private PhysicalSubscription<?> determineSinkSubscription() {
		ISource<?> startOperator = (ISource<?>)getStartNode().getContent();
		ISink<?> endOperator = (ISink<?>)getEndNode().getContent();
		
		for( PhysicalSubscription<?> sub : startOperator.getSubscriptions()) {
			if( sub.getTarget() == endOperator ) {
				return sub;
			}
		}
		
		// normally, we are not here now
		throw new RuntimeException("Could not find the subscription from source to sink");
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
		return other.getStartNode().equals(getStartNode()) && other.getEndNode().equals(getEndNode()) && other.sourceSubscription.equals(sourceSubscription);
	}
	
	public PhysicalSubscription<?> getSubscriptionToSink() {		
		return sinkSubscription;
	}
	
	public PhysicalSubscription<?> getSubscriptionToSource() {
		return sourceSubscription;
	}
}
