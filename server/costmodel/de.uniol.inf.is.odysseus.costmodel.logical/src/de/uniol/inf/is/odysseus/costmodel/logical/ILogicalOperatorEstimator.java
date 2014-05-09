package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;

public interface ILogicalOperatorEstimator<T extends ILogicalOperator> {

	public Collection<Class<? extends T>> getOperatorClasses();
	
	public void estimateLogical( T operator, Map<ILogicalOperator, DetailCost> previousCostMap, Map<SDFAttribute, IHistogram> histogramMap, ICostModelKnowledge knowledge );
	
	public double getMemory();
	public double getCpu();
	public double getNetwork();
	public double getSelectivity();
	public double getDatarate();
	public double getWindowSize();
}
