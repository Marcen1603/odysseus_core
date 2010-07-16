package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;

/**
 * This visitor finds all roots of a physical plan
 * that have no owner. These are the roots of the
 * current query. If they have already an owner,
 * then they belong to another query, so that
 * they will not be returned.
 * @author André Bolles
 *
 */
public class FindQueryRootsVisitor<P extends IPhysicalOperator> implements IGraphNodeVisitor<P, ArrayList<P>>{

	ArrayList<P> foundRoots;
	
	public FindQueryRootsVisitor(){
		this.foundRoots = new ArrayList<P>();
	}
	
	@Override
	public void afterFromSinkToSourceAction(P sink, P source) {
	}

	@Override
	public void afterFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public void beforeFromSinkToSourceAction(P sink, P source) {
		
	}

	@Override
	public void beforeFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public ArrayList<P> getResult() {
		// TODO Auto-generated method stub
		return this.foundRoots;
	}

	@Override
	public void nodeAction(P node) {
		// if the sink is only a sink or is a pipe and has no following
		// operators than it is a root. If this root has no owner,
		// it belongs to the current query and is not used by other
		// queries through plan sharing.
		if((node.isSink() && !node.isSource() || node.isPipe() && ((IPipe)node).getSubscriptions().isEmpty()) &&  !node.hasOwner()){
			this.foundRoots.add(node);
		}
		
	}

}
