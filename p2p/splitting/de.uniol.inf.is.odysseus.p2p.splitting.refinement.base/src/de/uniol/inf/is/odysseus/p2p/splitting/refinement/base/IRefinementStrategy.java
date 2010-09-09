package de.uniol.inf.is.odysseus.p2p.splitting.refinement.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public interface IRefinementStrategy {
	public ArrayList<ILogicalOperator> refinePlan(ArrayList<ILogicalOperator> ao);
	public String getName();
}
