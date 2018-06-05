package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class UnionAOEstimator extends StandardLogicalOperatorEstimator<UnionAO> {

	@Override
	protected Class<? extends UnionAO> getOperatorClass() {
		return UnionAO.class;
	}
	
	@Override
	public double getDatarate() {
		double unionDataRate = 0.0;
		for (DetailCost prevCost : getPrevCostMap().values()) {
			unionDataRate += prevCost.getDatarate();
		}
		return unionDataRate;
	}

	@Override
	public double getWindowSize() {
		double temp = 0.0;
		for (DetailCost prevCost : getPrevCostMap().values()) {
			temp += (prevCost.getWindowSize() * prevCost.getDatarate());
		}
		return temp / getDatarate();
	}
	
	@Override
	public double getMemory() {
		double minRate = Double.MAX_VALUE;
		double maxRate = Double.MIN_VALUE;
		for (DetailCost prevCost : getPrevCostMap().values()) {
			double rate = prevCost.getDatarate();
			if (rate > maxRate) {
				maxRate = rate;
			}
			if (rate < minRate) {
				minRate = rate;
			}
		}

		int sizeOfTuple = EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
		return (maxRate / minRate) * sizeOfTuple;
	}
}
