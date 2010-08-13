package de.uniol.inf.is.odysseus.util;

import java.util.HashMap;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class CopyPhysicalGraphVisitor<T extends IPhysicalOperator> implements IGraphNodeVisitor<T,T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	HashMap<T,T> nodeCopies;
	
	/**
	 * This is the first node, this visitor has ever seen.
	 * This is used to return as result, the same
	 * root in the copy of the plan as passed before
	 * copying.
	 */
	T root;
	
	public CopyPhysicalGraphVisitor(){
		this.nodeCopies = new HashMap<T,T>();
	}
	
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getResult() {
		// copy the logical plan
		for(Entry<T,T> entry: nodeCopies.entrySet()){
			T original = entry.getKey();
			T copy = entry.getValue();
			
			// it should be enough, to copy only
			// the incoming subscriptions, since
			// for each incoming subscription there
			// also exists an outgoing in the preceding
			// operator.
			if(original.isSink()){
				for(PhysicalSubscription sub: ((ISink<IPhysicalOperator>)original).getSubscribedToSource()){
					// get the copy of each target
					ISource<IPhysicalOperator> targetCopy = (ISource<IPhysicalOperator>)this.nodeCopies.get(sub.getTarget());
					// subscribe the target copy to the node copy
					((ISink<IPhysicalOperator>)copy).subscribeToSource(targetCopy, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				}
			}
		}
		
		
		// return the copy of the first visited node
		// because this is the node, from which
		// all following actions start
		return this.nodeCopies.get(this.root); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nodeAction(T op) {
		if(this.root == null){
			this.root = op;
		}
		if(!this.nodeCopies.containsKey(op)){
			T opCopy = (T)op.clone();
			if(op.isSink()){
				((ISink<IPhysicalOperator>)opCopy).unsubscribeFromAllSources();
			}

			// if it is also a source,
			// it should not be necessary any more
			// to unsubscribe from all sinks, since
			// if each sink unsubscribes from all its
			// sources, than at the end there will
			// be no sink any more, that is subscribed
			// to this operator.
			
			this.nodeCopies.put(op, opCopy);
		}
	}



}
