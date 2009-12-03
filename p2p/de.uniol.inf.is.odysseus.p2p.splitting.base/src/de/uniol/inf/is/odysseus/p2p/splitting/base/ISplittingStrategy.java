package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public interface ISplittingStrategy {
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator ao);
	public String getName();
}
