package de.uniol.inf.is.odysseus.util;

import java.util.List;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.IOwnedOperator;

public class SetOwnerGraphVisitor<T extends IOwnedOperator> implements
		IGraphNodeVisitor<T, T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	List<T> visited;

	/**
	 * This is the first node, this visitor has ever seen. This is used to
	 * return as result, the same root in the copy of the plan as passed before
	 * copying.
	 */
	T root;

	private IOperatorOwner owner;

	public SetOwnerGraphVisitor(IOperatorOwner owner) {
		this.visited = new IdentityArrayList<T>();
		this.owner = owner;
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
	}

	@Override
	public T getResult() {
		return null;
	}

	@Override
	public void nodeAction(T op) {
		if (!this.visited.contains(op)) {
			op.addOwner(owner);
			this.visited.add(op);
		}
	}

}
