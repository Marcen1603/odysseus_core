package de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.splitting;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public interface ISplittingStrategy {
	ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan);
	
	String getName();
}
