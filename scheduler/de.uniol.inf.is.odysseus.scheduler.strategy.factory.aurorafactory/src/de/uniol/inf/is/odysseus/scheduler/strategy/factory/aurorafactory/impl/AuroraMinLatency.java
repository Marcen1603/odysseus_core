package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class AuroraMinLatency extends AbstractExecListScheduling {

	public AuroraMinLatency(IPartialPlan plan) {
		super(plan);
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan plan) {
		List<IIterableSource<?>> toSchedule = plan.getIterableSource();

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
