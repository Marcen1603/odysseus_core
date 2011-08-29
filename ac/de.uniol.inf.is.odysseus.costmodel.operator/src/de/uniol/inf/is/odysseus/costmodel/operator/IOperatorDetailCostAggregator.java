package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IOperatorDetailCostAggregator {

	public AggregatedCost aggregate( Map<IPhysicalOperator, OperatorEstimation> operatorEstimations);
}
