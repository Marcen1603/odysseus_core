package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IPhysicalCostModel {

	public IPhysicalCost estimateCost( Collection<IPhysicalOperator> logicalOperators );

}
