package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.costmodel.PredicateSelectivityHelper;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class SelectAOEstimator extends StandardLogicalOperatorEstimator<SelectAO> {

	private static final double CPU_COST_FACTOR = 5.0;

	@Override
	protected Class<? extends SelectAO> getOperatorClass() {
		return SelectAO.class;
	}
	
	@Override
	public double getCpu() {
		return ( super.getCpu() * CPU_COST_FACTOR ) * super.getDatarate();
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
