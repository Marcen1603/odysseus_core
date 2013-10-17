package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;

public class CostConverter {
	public static ICost<IPhysicalOperator> calculateBearableCost(ICost<IPhysicalOperator> currentCost, double currentUsage, double threshold, ICostModel<IPhysicalOperator> costModel) {
		double mult;
		// the cost of the current plan can't be ranked as zero and the currentUsage can't be zero either
		if(currentCost.compareTo(costModel.getZeroCost()) != 0 && currentUsage != 0) {
			mult = threshold/currentUsage;
		// if one of the conditions doesn't hold, we can assume that the host can bear the maximum cost
		} else {
			return costModel.getMaximumCost();
		}
		ICost<IPhysicalOperator> thresholdCost = currentCost.fraction(mult);
		return thresholdCost.substract(currentCost);
	}
}
