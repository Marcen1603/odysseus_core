package de.uniol.inf.is.odysseus.planmanagement.optimization.installprocesscallsmonitor;

import de.uniol.inf.is.odysseus.monitoring.physicalplan.ProcessCallsMonitor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimitzationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class InstallProcessCallsMonitor implements IPostOptimitzationAction {

	@Override
	public void run(IQuery query, OptimizationConfiguration parameter) {
		query.addPlanMonitor("SLA Monitor", new ProcessCallsMonitor(query,
				false, "SLA Monitor",true));
	}

}
