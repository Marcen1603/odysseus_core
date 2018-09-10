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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.cost;

import de.uniol.inf.is.odysseus.core.server.sla.SLA;

/**
 * this is a quadratic cost function for metric latency and scope number
 * 
 * @author Thomas Vogelgesang
 *
 */
public class QuadraticCFLatencySingle extends QuadraticCFLatency {

	/**
	 * calculates opportunity costs for metric latency with scope single by
	 * quadratic cost function
	 */
	@Override
	public double oc(double conformance, SLA sla) {
		// determine index of current service level
		int slIndex = this.getCurrentServiceLevelIndex(conformance, sla);
		
		/* 
		 * calculate oc if current service level is not the less valuable 
		 * service level, return 0 otherwise
		 */
		if (slIndex < sla.getServiceLevel().size()) {
			// calculate lower bound
			double lowerbound = this.calcLowerbound(slIndex, sla);
			
			// calculate upper bound
			double upperbound = this.calcUpperbound(slIndex, sla);
			
			// calculate delta to lower service level
			double delta = this.calcDelta(slIndex, sla);
			
			// calculate relative position in service level:
			double temp = (upperbound - conformance) / (upperbound - lowerbound);
			// square relative position 
			temp = temp * temp;
			/* 
			 * multiply cost with relative position and add half of penalty for 
			 * violation of current service level
			 */
			return temp * delta + 0.5 * delta;
		}
        // TODO replace this by 0 and define time-dependant mg() function?
        return sla.getMaxPenalty() * conformance;
		
	}

	/**
	 * calculates marginal gain for metric latency with scope single by
	 * quadratic cost function
	 */
	@Override
	public double mg(double conformance, SLA sla) {
		/* 
		 * in scope single there is no chance to return to a better service
		 * level
		 */
		return 0;
	}
	
}

