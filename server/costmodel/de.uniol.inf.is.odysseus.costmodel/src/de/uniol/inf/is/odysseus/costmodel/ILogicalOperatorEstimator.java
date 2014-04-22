package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface ILogicalOperatorEstimator<T extends ILogicalOperator> {

	public Collection<? extends Class<T>> getOperatorClasses();
	
	public void estimateLogical( T operator, Map<ILogicalOperator, DetailCost> previousCostMap );
	
	public double getMemory();
	public double getCpu();
	public double getNetwork();
	public double getSelectivity();
	public double getDatarate();
	public double getWindowSize();
}
