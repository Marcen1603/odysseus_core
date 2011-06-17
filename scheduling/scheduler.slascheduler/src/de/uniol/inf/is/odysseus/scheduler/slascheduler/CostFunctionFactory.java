package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Average;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Single;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyNumber;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencySingle;

public class CostFunctionFactory {
	
	public static final String QUADRATIC_COST_FUNCTION = "quadratic";
	
	public CostFunctionFactory() {
		
	}
	
	public ICostFunction createCostFunction(String functionName, SLA sla) {
		/*
		 * cost functions (e.g. QuadraticCostFunction) can be different for
		 * each metric and each scope
		 */
		ICostFunction function = null;
		if (QUADRATIC_COST_FUNCTION.equals(functionName)) {
			if (sla.getMetric() instanceof Latency) {
				if (sla.getScope() instanceof Average) {
					function = new QuadraticCFLatencyAverage();
				} else if (sla.getScope() instanceof Number) {
					function = new QuadraticCFLatencyNumber();
				} else if (sla.getScope() instanceof Single) {
					function = new QuadraticCFLatencySingle();
				}
			}
		}
		return function;
	}

}
