package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public class OdysseusConnectionModel extends DefaultConnectionModel<IPhysicalOperator> {

	private final AbstractPhysicalSubscription<?,?> sourceSubscription;
	private final AbstractPhysicalSubscription<?,?> sinkSubscription;
	
	public OdysseusConnectionModel(INodeModel<IPhysicalOperator> startNode, INodeModel<IPhysicalOperator> endNode) {
		super(startNode, endNode);
		
		this.sourceSubscription = determineSourceSubscription();
		this.sinkSubscription = determineSinkSubscription();
	}

	private AbstractPhysicalSubscription<?,?> determineSourceSubscription() {
		ISource<?> startOperator = (ISource<?>)getStartNode().getContent();
		ISink<?> endOperator = (ISink<?>)getEndNode().getContent();
		
		for( AbstractPhysicalSubscription<?,?> sub : endOperator.getSubscribedToSource()) {
			if( sub.getSource() == startOperator ) {
				return sub;
			}
		}
		
		// normally, we are not here now
		throw new RuntimeException("Could not find the subscription from source to sink");
	}

	private AbstractPhysicalSubscription<?,?> determineSinkSubscription() {
		ISource<?> startOperator = (ISource<?>)getStartNode().getContent();
		ISink<?> endOperator = (ISink<?>)getEndNode().getContent();
		
		for( AbstractPhysicalSubscription<?,?> sub : startOperator.getSubscriptions()) {
			if( sub.getSink() == endOperator ) {
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
	
	public AbstractPhysicalSubscription<?,?> getSubscriptionToSink() {		
		return sinkSubscription;
	}
	
	public AbstractPhysicalSubscription<?,?> getSubscriptionToSource() {
		return sourceSubscription;
	}
}
