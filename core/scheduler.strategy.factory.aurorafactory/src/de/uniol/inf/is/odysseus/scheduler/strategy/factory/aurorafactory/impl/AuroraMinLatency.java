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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.collection.FESortedPair;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;

public class AuroraMinLatency extends AbstractExecListScheduling {

	public AuroraMinLatency(IPartialPlan plan) {
		super(plan);
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan plan) {
		List<IIterableSource<?>> toSchedule = plan.getIterableSources();

		List<IIterableSource<?>> execList = init(toSchedule);

		return execList;
	}

	@Override
	public void applyChangedPlan()
	{
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}
	
	protected void initMetadata(List<ISource<?>> opPath) {
		for (ISource<?> s : opPath) {
			if (!s.providesMonitoringData(MonitoringDataTypes.ESTIMATED_PRODUCTIVITY.name)) {
				s.addMonitoringData(MonitoringDataTypes.ESTIMATED_PRODUCTIVITY.name,
						new StaticValueMonitoringData<Double>(s,
								MonitoringDataTypes.ESTIMATED_PRODUCTIVITY.name, 1d));
			}

			if (!s.providesMonitoringData(MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name)) {
				s.addMonitoringData(MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name,
						new StaticValueMonitoringData<Double>(s,
								MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name, 1d));
			}
		}
	}	
	
	private List<IIterableSource<?>> init(List<IIterableSource<?>> toSchedule) {
		List<FESortedPair<Double,List<IIterableSource<?>>>> pathes = new LinkedList<FESortedPair<Double,List<IIterableSource<?>>>>();
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		// Calc for every to schedule operator the path to the root;
		for (ISource<?> s:toSchedule){
			List<IIterableSource<?>> schPath = new LinkedList<IIterableSource<?>>();
			List<ISource<?>> opPath = new LinkedList<ISource<?>>();
			getPathToRoot(s, schPath, opPath, null);
			initMetadata(opPath);
			FESortedPair<Double, List<IIterableSource<?>>> pWithCost = new FESortedPair<Double, List<IIterableSource<?>>>(calcPathCost(opPath), schPath);
			//System.out.println("Path Costs :"+opPath+" --> "+pWithCost);
			pathes.add(pWithCost);
		}			
		// Sort pathes respecting cost
		Collections.sort(pathes);
		
		// Add Pathes to execList
		for (FESortedPair<Double,List<IIterableSource<?>>> p:pathes){
			//System.out.println(p);
			execList.addAll(p.getE2());
		}
		return execList;
	}
	
	

	

}
