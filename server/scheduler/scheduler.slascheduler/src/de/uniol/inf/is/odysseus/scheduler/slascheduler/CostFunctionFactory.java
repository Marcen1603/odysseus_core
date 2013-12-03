/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Latency;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Average;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Number;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Rate;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Single;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyNumber;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyRate;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencySingle;

/**
 * Factory for building cost functions
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class CostFunctionFactory {
	/**
	 * id of quadratic cost function
	 */
	public static final String QUADRATIC_COST_FUNCTION = "quadratic";

	/**
	 * builds a new cost function according to the given function name and the
	 * given sla
	 * 
	 * @param functionName
	 *            the name of the cost function
	 * @param sla
	 *            the sla
	 * @return the new cost function object or null if no matching object could
	 *         be created
	 */
	public ICostFunction createCostFunction(String functionName, SLA sla) {
		/*
		 * cost functions (e.g. QuadraticCostFunction) can be different for each
		 * metric and each scope
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
				} else if (sla.getScope() instanceof Rate) {
					function = new QuadraticCFLatencyRate();
				}
			}
		}
		return function;
	}

}
