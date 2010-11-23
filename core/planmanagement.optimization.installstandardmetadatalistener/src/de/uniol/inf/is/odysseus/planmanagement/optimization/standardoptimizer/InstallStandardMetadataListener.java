package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.PlanMonitor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class InstallStandardMetadataListener implements IPostOptimizationAction {

	// TODO: Konfigurierbar machen
	private static long MONITORING_PERIOD = 30000;

	@Override
	public void run(IQuery query, OptimizationConfiguration parameter) {
		query.addPlanMonitor(MonitoringDataTypes.DATARATE.name,
				new PlanMonitor(query, false,false,
						MonitoringDataTypes.DATARATE.name,
						MONITORING_PERIOD));
		query.addPlanMonitor(MonitoringDataTypes.SELECTIVITY.name,
				new PlanMonitor(query, false,false,
						MonitoringDataTypes.SELECTIVITY.name, -1));
	}

}
