package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;
import de.uniol.inf.is.odysseus.costmodel.IHistogram;

public interface IPhysicalOperatorEstimator<T extends IPhysicalOperator> {

	public Collection<Class<? extends T>> getOperatorClasses();
	
	public void estimatePhysical( T operator, Map<IPhysicalOperator, DetailCost> previousCostMap, Map<SDFAttribute, IHistogram> histograms, ICostModelKnowledge knowledge );
	
	public double getMemory();
	public double getCpu();
	public double getNetwork();
	public double getSelectivity();
	public double getDatarate();
	public double getWindowSize();
}
