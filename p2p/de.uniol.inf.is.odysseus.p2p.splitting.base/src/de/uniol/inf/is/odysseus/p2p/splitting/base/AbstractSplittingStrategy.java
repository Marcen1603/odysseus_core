package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.splitting.refinement.base.IRefinementStrategy;

public abstract class AbstractSplittingStrategy implements ISplittingStrategy {

	private IRefinementStrategy refinement = null;

	@Override
	public abstract ArrayList<AbstractLogicalOperator> splitPlan(
			AbstractLogicalOperator ao);

	@Override
	public abstract String getName();

	public void bindRefinement(IRefinementStrategy rs) {
		System.out.println("Refinement eingebunden");
		this.refinement = rs;
	}

	public void unbindRefinement(IRefinementStrategy rs) {
		if(this.refinement == rs) {
			this.refinement = null;
		}
	}
	
	public IRefinementStrategy getRefinement() {
		return refinement;
	}

}
