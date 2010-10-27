package de.uniol.inf.is.odysseus.planmanagement;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;


public interface IRewrite {
	public ILogicalOperator rewritePlan(ILogicalOperator plan, RewriteConfiguration conf);
}
