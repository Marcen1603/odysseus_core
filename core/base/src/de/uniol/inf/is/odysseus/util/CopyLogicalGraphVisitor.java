package de.uniol.inf.is.odysseus.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;

public class CopyLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	Map<T, T> nodeCopies;

	/**
	 * This is the first node, this visitor has ever seen. This is used to
	 * return as result, the same root in the copy of the plan as passed before
	 * copying.
	 */
	T root;

	private IOperatorOwner owner;

	public CopyLogicalGraphVisitor(IOperatorOwner owner) {
		this.nodeCopies = new IdentityHashMap<T, T>();
		this.owner = owner;
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
		for (Entry<T, T> entry : nodeCopies.entrySet()) {
			T original = entry.getKey();
			T copy = entry.getValue();

			// it should be enough, to copy only
			// the incoming subscriptions, since
			// for each incoming subscription there
			// also exists an outgoing in the preceding
			// operator.
			for (LogicalSubscription sub : original.getSubscribedToSource()) {
				// get the copy of each target
				T targetCopy = this.nodeCopies.get(sub.getTarget());
				// subscribe the target copy to the node copy
				// if its in the same plan, add copy
				if (targetCopy != null) {
					copy.subscribeToSource(targetCopy, sub.getSinkInPort(),
							sub.getSourceOutPort(), sub.getSchema());			
				}
			}
		}

		return this.nodeCopies.get(this.root);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nodeAction(T op) {
		// Copy Plan only for owners (if available)
		if (!op.hasOwner() || op.isOwnedBy(owner)) {
			if (this.root == null) {
				this.root = op;
			}
			if (!this.nodeCopies.containsKey(op)) {
				T opCopy = (T) op.clone();
				opCopy.clearPhysicalSubscriptions();
				this.nodeCopies.put(op, opCopy);
			}
		}
	}

}
