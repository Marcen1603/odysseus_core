package de.uniol.inf.is.odysseus.core.server.util;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;


/**
 * Generic Graph walker, that only walks in dataflow direction
 * @author ChrisToenjesDeye
 *
 * @param <S>
 */
public class GenericDownstreamGraphWalker<S extends ISubscriber<S, ? extends ISubscription<S>> & ISubscribable<S, ? extends ISubscription<S>>>
		extends GenericGraphWalker<S> {
	
	@Override
	public void prefixWalk(S node, IGraphNodeVisitor<S, ?> visitor) {
		if(this.visited.contains(node)){				
			return;
		}		
		this.visited.add(node);
		
		visitor.nodeAction(node);
		
		for(ISubscription<S> s: node.getSubscriptions()){
			visitor.beforeFromSourceToSinkAction(node, s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.afterFromSourceToSinkAction(node, s.getTarget());
		}
	}
	
	@Override
	public void prefixWalkPhysical(IPhysicalOperator node, IGraphNodeVisitor<IPhysicalOperator, ?> visitor){
		if(this.visitedPhysical.contains(node)){
			return;
		}
		
		this.visitedPhysical.add(node);
		
		visitor.nodeAction(node);
			
		if(node.isSource()){
			for(AbstractPhysicalSubscription<?> s: ((ISource<?>)node).getSubscriptions()){
				IPhysicalOperator t = (IPhysicalOperator)s.getTarget();
				visitor.beforeFromSourceToSinkAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSourceToSinkAction(node, t);
			}
		}
	}
}
