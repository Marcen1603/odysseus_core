package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.costmodel.opcount.OpCountCost;

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
	
//	public static double projectedUsageUsingOperatorCost(OperatorCost<IPhysicalOperator> currentCost, double currentUsage, OperatorCost<IPhysicalOperator> newPlanCost) {
//		OperatorCost<IPhysicalOperator> merged = (OperatorCost<IPhysicalOperator>) currentCost.merge(newPlanCost);
//		OperatorCost<IPhysicalOperator> currentCostAsOperatorCost = (OperatorCost<IPhysicalOperator>)currentCost;
//		double cpuFactor = 1;
//		double memFactor = 1;
//		if(merged.getCpuCost() != 0. && currentCostAsOperatorCost.getCpuCost() != 0.) {
//			cpuFactor = merged.getCpuCost() / currentCostAsOperatorCost.getCpuCost();
//		}
//		if(merged.getMemCost() != 0. && currentCostAsOperatorCost.getMemCost() != 0.) {
//			memFactor = merged.getMemCost() / currentCostAsOperatorCost.getMemCost();
//		}
//		return currentUsage * (cpuFactor+memFactor)/2;
//	}
	
	public static double projectedUsageUsingOpCountCost(OpCountCost<IPhysicalOperator> currentCost, double currentUsage, OpCountCost<IPhysicalOperator> newPlanCost) {
		OpCountCost<IPhysicalOperator> merged = (OpCountCost<IPhysicalOperator>) currentCost.merge(newPlanCost);
		OpCountCost<IPhysicalOperator> currentCostAsOpCountCost = (OpCountCost<IPhysicalOperator>)currentCost;
		double currentOpCount = currentCostAsOpCountCost.getOpCount();
		double mergedOpCount = merged.getOpCount();
		double factor = currentOpCount != 0 ? mergedOpCount/currentOpCount : 1;
		return currentUsage * factor;
	}
	
}
