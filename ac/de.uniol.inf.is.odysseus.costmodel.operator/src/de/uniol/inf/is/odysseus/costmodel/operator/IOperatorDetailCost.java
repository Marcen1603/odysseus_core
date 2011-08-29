package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IOperatorDetailCost {

	public IPhysicalOperator getOperator();
	
	public double getMemoryCost();
	public double getProcessorCost();
	
}
