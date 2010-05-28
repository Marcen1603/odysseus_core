package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;

public class GreedyScheduling extends AbstractExecListScheduling {

	public GreedyScheduling(IPartialPlan plan) {
		super(plan);
	}

	/**
	 * 
	 * @param p
	 * @return Pair<Costs,Seletivity>
	 */
	private void calcVirtOpCosts(List<ISource<?>> p, Helper ret) {
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
		calcForLeafsPathsToRoots(plan.getRoots(), virtualOps, pathes);
		Helper h = new Helper();
		PriorityQueue<FESortedPair<Double,IIterableSource<?>>> prios = new PriorityQueue<FESortedPair<Double,IIterableSource<?>>>();
		
		for (IIterableSource<?>s: plan.getIterableSource()) {
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