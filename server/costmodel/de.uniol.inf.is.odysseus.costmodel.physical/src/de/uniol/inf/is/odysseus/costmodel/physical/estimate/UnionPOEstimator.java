package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class UnionPOEstimator extends StandardPhysicalOperatorEstimator<UnionPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<UnionPO>> getOperatorClasses() {
		return Lists.newArrayList(UnionPO.class);
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
