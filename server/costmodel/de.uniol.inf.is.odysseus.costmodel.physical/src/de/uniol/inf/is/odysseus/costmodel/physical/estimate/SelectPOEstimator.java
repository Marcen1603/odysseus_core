package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.util.PredicateSelectivityHelper;

@SuppressWarnings("rawtypes")
public class SelectPOEstimator extends StandardPhysicalOperatorEstimator<SelectPO> {

	private static final double CPU_COST_FACTOR = 5.0;
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<SelectPO>> getOperatorClasses() {
		return Lists.newArrayList(SelectPO.class);
	}

	@Override
	public double getCpu() {
		return super.getCpu() * CPU_COST_FACTOR;
	}
	
	@Override
	public double getMemory() {
		return super.getMemory() + 4;
	}
	
	@Override
	public double getDatarate() {
		return super.getDatarate() * getSelectivity();
	}
	
	@Override
	public double getSelectivity() {
		PredicateSelectivityHelper helper = new PredicateSelectivityHelper(getOperator().getPredicate(),getHistogramMap());
		Optional<Double> optSelectivity = helper.getSelectivity();
		return optSelectivity.isPresent() ? optSelectivity.get() : super.getSelectivity();
	}
}
