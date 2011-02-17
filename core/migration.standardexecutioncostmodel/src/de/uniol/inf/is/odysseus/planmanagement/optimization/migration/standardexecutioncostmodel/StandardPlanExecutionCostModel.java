/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardexecutioncostmodel;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.access.ByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanExecutionCostModel;

/**
 * Defines execution cost of physical operators.
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanExecutionCostModel implements IPlanExecutionCostModel {
	
	private Map<Class<?>, StandardPlanExecutionCost> operatorCost;

	public StandardPlanExecutionCostModel() {
		this.operatorCost = new HashMap<Class<?>, StandardPlanExecutionCost>();
		
		// TODO: Fein-Tuning von score Werten erfordert Evaluation, welche Plaene mit welchen Daten wieviel Kosten verursachen.
		
		// base
		this.operatorCost.put(SelectPO.class, 				new StandardPlanExecutionCost(1, 20, 1, 1));
		this.operatorCost.put(MetadataCreationPO.class, 	new StandardPlanExecutionCost(1, 5, 1, 1));
		// access
		this.operatorCost.put(ByteBufferReceiverPO.class, 	new StandardPlanExecutionCost(1, 6, 1, 1));
		// intervalapproach
		this.operatorCost.put(JoinTIPO.class, 				new StandardPlanExecutionCost(1, 30, 10, 1));
		this.operatorCost.put(SlidingTimeWindowTIPO.class, 	new StandardPlanExecutionCost(10, 6, 2, 1));
		// relational
		this.operatorCost.put(RelationalProjectPO.class, 	new StandardPlanExecutionCost(1, 2, 1, 1));
	}
	
	@Override
	public ICostCalculator<IPhysicalOperator> getCostCalculator() {
		return new StandardPlanExecutionCostCalculator(this);
	}
	
	StandardPlanExecutionCost getCost(IPhysicalOperator op) {
		return new StandardPlanExecutionCost(this.operatorCost.get(op.getClass()));
	}
	
	void calculateScore(StandardPlanExecutionCost cost) {
		cost.setScore((int)(1.0f * cost.getCpuTime()
				+ 0.3f * cost.getLatency()
				+ 0.1f * cost.getMemoryConsumption()
				+ 0.05f * cost.getNetworkBandwidth()));
	}
}
