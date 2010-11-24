package de.uniol.inf.is.odysseus.util;

import java.util.List;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;

public class AbstractGraphWalker<R, S extends ISubscriber<S, H> & ISubscribable<S, H>, H extends ISubscription<S>>{

	List<S> visited;
	List<IPhysicalOperator> visitedPhysical;
	
	public AbstractGraphWalker(){
		this.visited = new IdentityArrayList<S>();
		this.visitedPhysical = new IdentityArrayList<IPhysicalOperator>();
	}
	
	public void clearVisited(){
		this.visited = new IdentityArrayList<S>();
		this.visitedPhysical = new IdentityArrayList<IPhysicalOperator>();
	}
	
	/**
	 * ascendAction() and descendAction() cannot be used in this graph walker.
	 * This is because, we have a directed graph. Some nodes are reachable
	 * only against data direction and other nodes are only reachable in
	 * data data direction.
	 * 
	 * Assume the following graph:
	 * 
	 * A <- B -> C
	 * 
	 * The arrows mark the data direction of the operators.
	 * If we only know A, we can reach B only against data direction and C only
	 * in data direction. Have in mind, that subscriptions can be traversed in
	 * both directions, but they have a semantically determined data direction.
	 * 
	 * @param node
	 * @param visitor
	 * @return
	 */
	public /*<R,T extends ISubscriber, H extends ISubscription<T>>*/ void prefixWalk(S node, IGraphNodeVisitor<S, R> visitor) {
		if(this.visited.contains(node)){
			return;
		}
		else{
			this.visited.add(node);
		}
		
		visitor.nodeAction(node);
		
		for (H s: node.getSubscribedToSource()){
			visitor.beforeFromSinkToSourceAction(node, s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.afterFromSinkToSourceAction(node, s.getTarget());
		}
		
		for(H s: node.getSubscriptions()){
			visitor.beforeFromSourceToSinkAction(node, s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.afterFromSourceToSinkAction(node, s.getTarget());
		}
	}
	
	public void prefixWalkPhysical(IPhysicalOperator node, IGraphNodeVisitor<IPhysicalOperator, R> visitor){
		if(this.visitedPhysical.contains(node)){
			return;
		}
		else{
			this.visitedPhysical.add(node);
		}
		
		visitor.nodeAction(node);
		
		if(node.isSink()){
			for (PhysicalSubscription<?> s : ((ISink<?>)node).getSubscribedToSource()){
				IPhysicalOperator t = (IPhysicalOperator) s.getTarget();
				visitor.beforeFromSinkToSourceAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSinkToSourceAction(node, t);
			}
		}
			
		if(node.isSource()){
			for(PhysicalSubscription<?> s: ((ISource<?>)node).getSubscriptions()){
				IPhysicalOperator t = (IPhysicalOperator)s.getTarget();
				visitor.beforeFromSourceToSinkAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSourceToSinkAction(node, t);
			}
		}
	}
	
//	public static <R> R prefixWalk2(IPhysicalOperator node, INodeVisitor<IPhysicalOperator,R> visitor) {
//		visitor.nodeAction(node);
//		if (!node.isSink()) {
//			return null;
//		}
//		for (PhysicalSubscription<?> s : ((ISink<?>)node).getSubscribedToSource()){
//			IPhysicalOperator t = (IPhysicalOperator) s.getTarget();
//			visitor.descendAction(t);
//			prefixWalk2(t, visitor);
//			visitor.ascendAction(node);
//		}
//		return visitor.getResult();
//	}
}
