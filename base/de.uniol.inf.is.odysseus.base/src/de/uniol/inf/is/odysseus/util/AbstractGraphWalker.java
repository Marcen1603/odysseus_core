package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ISubscribable;
import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.ISubscription;

public class AbstractGraphWalker<R, S extends ISubscriber<S, H> & ISubscribable<S, H>, H extends ISubscription<S>>{

	ArrayList<S> visited;
	
	public AbstractGraphWalker(){
		this.visited = new ArrayList<S>();
	}
	
	public void clearVisited(){
		this.visited = new ArrayList<S>();
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
