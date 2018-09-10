package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.costmodel.PredicateSelectivityHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class SelectPOEstimator extends StandardPhysicalOperatorEstimator<SelectPO> {

	private static final double CPU_COST_FACTOR = 5.0;
	
	@Override
	public Class<? extends SelectPO> getOperatorClass() {
		return SelectPO.class;
	}

	@Override
	public double getCpu() {
		//super.getCpu already is multiploed with Datarate... 
		return ( super.getCpu() * CPU_COST_FACTOR ); //* super.getDatarate();
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
