/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.AbstractExecListScheduling;

public class GreedyScheduling extends AbstractExecListScheduling {

	public GreedyScheduling(IPartialPlan plan) {
		super(plan);
	}

	/**
	 * 
	 * @param p
	 * @return Pair<Costs,Seletivity>
	 */
	private static void calcVirtOpCosts(List<ISource<?>> p, Helper ret) {
		double t = 0;
		double s = 1;
		if (p != null) {
			// Die Summe der Kosten des virtuellen Operators berechnen!!
			for (int i = 0; i < p.size(); i++) {
				IMonitoringData<Double> c = p.get(i).getMonitoringData(
						MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name);
				t = t + ((Number) c.getValue()).doubleValue();
				IMonitoringData<Double> sel = p.get(i).getMonitoringData(
						MonitoringDataTypes.ESTIMATED_SELECTIVITY.name);
				s = s * ((Number) sel.getValue()).doubleValue();
			}
		}
		ret.cost = t;
		ret.selectivity = s;
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan plan) {
		
		// Calc for every leaf (!) operator the path to the root (inkl. virtual operators)
		Map<IIterableSource<?>, List<ISource<?>>> virtualOps = new HashMap<IIterableSource<?>, List<ISource<?>>>();
		List<List<IIterableSource<?>>> pathes = new ArrayList<List<IIterableSource<?>>>();
		
		List<ISink<?>> sinkRoots = new ArrayList<ISink<?>>();
		for(IPhysicalOperator curRoot : plan.getRoots()){
			if(curRoot.isSink()){
				sinkRoots.add((ISink<?>)curRoot);
			}
		}
		
		calcForLeafsPathsToRoots(sinkRoots, virtualOps, pathes);
		Helper h = new Helper();
		PriorityQueue<FESortedPair<Double,IIterableSource<?>>> prios = new PriorityQueue<FESortedPair<Double,IIterableSource<?>>>();
		
		for (IIterableSource<?>s: plan.getIterableSources()) {
			calcVirtOpCosts(virtualOps.get(s), h);
			double priority = (1.0d - h.selectivity) /h.cost;
			prios.add(new FESortedPair<Double, IIterableSource<?>>(priority, s));	
		}
		
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		for (FESortedPair<Double,IIterableSource<?>> p:prios){
			execList.add(p.getE2());
		}
		return execList;
	}

	@Override
	public void applyChangedPlan() {
		calculateExecutionList(getPlan());
	}

}

class Helper{
	public double cost;
	public double selectivity;
}