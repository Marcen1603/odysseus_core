package de.uniol.inf.is.odysseus.planmanagement.optimization.installprocesscallsmonitor;

import de.uniol.inf.is.odysseus.monitoring.physicalplan.ProcessCallsMonitor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class InstallProcessCallsMonitor implements IPostOptimizationAction {

	@Override
	public void run(IQuery query, OptimizationConfiguration parameter) {
		query.addPlanMonitor("Root Monitor", new ProcessCallsMonitor(query,
				true,false, "Root Monitor",true));
		query.addPlanMonitor("Buffer Monitor", new ProcessCallsMonitor(query,
				false,true, "Buffer Monitor",true));

	}

}
