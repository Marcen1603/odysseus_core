package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface ILogicalCostModel {

	public ILogicalCost estimateCost( Collection<ILogicalOperator> logicalOperators );
	
}
