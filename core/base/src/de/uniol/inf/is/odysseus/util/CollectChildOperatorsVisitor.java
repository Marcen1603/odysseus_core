package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * This visitor finds children of a physical plan
 * @author Marco Grawunder
 *
 */
public class CollectChildOperatorsVisitor<P extends IPhysicalOperator> implements IGraphNodeVisitor<P, ArrayList<P>>{

	ArrayList<P> found;
	
	public CollectChildOperatorsVisitor(){
		this.found = new ArrayList<P>();
	}
	
	@Override
	public void afterFromSinkToSourceAction(P sink, P source) {
		this.found.add(source);
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
	}

}
