package de.uniol.inf.is.odysseus.core.util;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * Generic Graph walker, that only walks against dataflow direction
 * @author ChrisToenjesDeye
 *
 * @param <S>
 */
public class GenericUpstreamGraphWalker<S extends ISubscriber<S, ? extends ISubscription<S,?>> & ISubscribable<S, ? extends ISubscription<S,?>>>
		extends GenericGraphWalker<S> {
	
	@Override
	public void prefixWalk(S node, IGraphNodeVisitor<S, ?> visitor) {
		if(this.visited.contains(node)){				
			return;
		}		
		this.visited.add(node);
		
		visitor.nodeAction(node);
		
		for (ISubscription<S,?> s: node.getSubscribedToSource()){
			visitor.beforeFromSinkToSourceAction(node, s.getSource());
			prefixWalk(s.getSource(), visitor);
			visitor.afterFromSinkToSourceAction(node, s.getSource());
		}
	}
	
	@Override
	public void prefixWalkPhysical(IPhysicalOperator node, IGraphNodeVisitor<IPhysicalOperator, ?> visitor){
		if(this.visitedPhysical.contains(node)){
			return;
		}
		
		this.visitedPhysical.add(node);
		
		visitor.nodeAction(node);
		
		if(node.isSink()){
			for (AbstractPhysicalSubscription<?,?> s : ((ISink<?>)node).getSubscribedToSource()){
				IPhysicalOperator t = (IPhysicalOperator) s.getSource();
				visitor.beforeFromSinkToSourceAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSinkToSourceAction(node, t);
			}
		}
	}
}
