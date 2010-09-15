package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * This visitor finds all roots of a physical plan
 * that have no owner. These are the roots of the
 * current query. If they have already an owner,
 * then they belong to another query, so that
 * they will not be returned.
 * @author André Bolles
 *
 */
public class CollectOperatorsVisitor<P extends IPhysicalOperator> implements IGraphNodeVisitor<P, ArrayList<P>>{

	ArrayList<P> found;
	
	public CollectOperatorsVisitor(){
		this.found = new ArrayList<P>();
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
		return this.found;
	}

	@Override
	public void nodeAction(P node) {
		this.found.add(node);		
	}

}
