package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IPhysicalOperatorEstimator<T extends IPhysicalOperator> {

	public Collection<? extends Class<T>> getOperatorClasses();
	
	public void estimatePhysical( T operator, Map<IPhysicalOperator, DetailCost> previousCostMap );
	
	public double getMemory();
	public double getCpu();
	public double getNetwork();
	public double getSelectivity();
	public double getDatarate();
	public double getWindowSize();
}
