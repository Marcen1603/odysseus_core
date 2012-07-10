/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
