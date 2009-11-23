package de.uniol.inf.is.odysseus.p2p.splitting.refinement.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public interface IRefinementStrategy {
	public ArrayList<AbstractLogicalOperator> refinePlan(ArrayList<AbstractLogicalOperator> ao);
	public String getName();
}
