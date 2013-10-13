package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;

public class CostConverter {
	public static ICost<IPhysicalOperator> calculateBearableCost(ICost<IPhysicalOperator> currentCost, double currentUsage, double threshold, ICostModel<IPhysicalOperator> costModel) {
		double mult;
		if(currentUsage != 0) {
			mult = threshold/currentUsage;
		} else {
			return costModel.getMaximumCost();
		}
		ICost<IPhysicalOperator> thresholdCost = currentCost.fraction(mult);
		return thresholdCost.substract(currentCost);
	}
}
