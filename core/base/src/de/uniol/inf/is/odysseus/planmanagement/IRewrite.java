package de.uniol.inf.is.odysseus.planmanagement;

import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;


public interface IRewrite {
	public ILogicalOperator rewritePlan(ILogicalOperator plan);

	public ILogicalOperator rewritePlan(ILogicalOperator plan, Set<String> rulesToApply);
}
