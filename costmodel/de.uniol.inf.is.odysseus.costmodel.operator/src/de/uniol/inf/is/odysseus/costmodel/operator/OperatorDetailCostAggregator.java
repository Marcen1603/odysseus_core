package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Standardimplementierung der Aggregation der einzelnen Operatorkosten. Alle
 * Kostenbestandteile (Speicher, Prozessor) werden komponentenbasiert addiert.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorDetailCostAggregator implements IOperatorDetailCostAggregator {

	@Override
	public AggregatedCost aggregate(Map<IPhysicalOperator, OperatorEstimation> operatorEstimations) {
		// aggregate Costs
		double sumCpuCost = 0.0;
		double sumMemCost = 0.0;
		for (IPhysicalOperator op : operatorEstimations.keySet()) {
			IOperatorDetailCost cost = operatorEstimations.get(op).getDetailCost();
			sumCpuCost += cost.getProcessorCost();
			sumMemCost += cost.getMemoryCost();
		}

		return new AggregatedCost(sumCpuCost, sumMemCost);
	}

}
