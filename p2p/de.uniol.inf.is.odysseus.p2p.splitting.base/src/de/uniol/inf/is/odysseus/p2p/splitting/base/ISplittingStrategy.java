package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public interface ISplittingStrategy {
	public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator ao);
	public String getName();
}
