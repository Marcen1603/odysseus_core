package de.uniol.inf.is.odysseus.base;

import java.util.Set;


public interface IRewrite {
	public ILogicalOperator rewritePlan(ILogicalOperator plan);

	public ILogicalOperator rewritePlan(ILogicalOperator plan, Set<String> rulesToApply);
}
