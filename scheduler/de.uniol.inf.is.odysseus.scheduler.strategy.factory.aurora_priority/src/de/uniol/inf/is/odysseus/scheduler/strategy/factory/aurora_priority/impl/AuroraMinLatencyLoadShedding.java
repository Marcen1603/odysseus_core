package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority.impl;

import java.util.List;

import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class AuroraMinLatencyLoadShedding extends AbstractPriorityMinLatency{

	public AuroraMinLatencyLoadShedding(IPartialPlan plan) {
		super(plan);
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan plan) {
		
		LoadManager manager = LoadManager.getInstance(null);
		manager.addCapacities(plan);		
		
		List<IIterableSource<?>> toSchedule = plan.getIterableSource();
		List<IIterableSource<?>> execList = init(toSchedule);

		return execList;
	}

	@Override
	protected void executePriorisationActivation(ISource<?> source,
			List<ISource<?>> opPath) {
	}
	
	@Override	
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
			
			if (!s.providesMonitoringData(MonitoringDataTypes.SELECTIVITY.name)) {
				s.addMonitoringData(MonitoringDataTypes.SELECTIVITY.name,
						new StaticValueMonitoringData<Double>(s,
								MonitoringDataTypes.SELECTIVITY.name, 1d));
			}			
			
		}
	}	
	

}
