package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MedianProcessingTime;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class SLAPostOptimizationAction implements IPostOptimizationAction {

	@Override
	public void run(IPhysicalQuery query, OptimizationConfiguration parameter) {
		for (IPhysicalOperator operator : query.getPhysicalChilds()) {
			if (operator.isSink()) {
				if (operator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name) == null) {
					operator.addMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name, new MedianProcessingTime(operator));
				}
			}
		}

	}

}
