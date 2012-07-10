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

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Latency;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Average;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Number;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Rate;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Single;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyRateConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencySingleConformance;

/**
 * Factory for building sla conformance objects
 * 
 * @author Thomas Vogelgesang
 *
 */
@SuppressWarnings({"rawtypes"})
public class SLAConformanceFactory {

	/**
	 * creates a new SLAConformance object for a given SLA
	 * 
	 * @param sla
	 * @return a new SLAConformance object or null, if no matching
	 *         SLAConformance object for the given sla exists
	 */
	public ISLAConformance createSLAConformance(SLA sla, 
			ISLAViolationEventDistributor distributor, IPhysicalQuery query) {
		ISLAConformance conformance = null;

		if (sla.getMetric() instanceof Latency) {
			if (sla.getScope() instanceof Average) {
				conformance = new LatencyAverageConformance(distributor, sla, 
						query);
			} else if (sla.getScope() instanceof Number) {
				conformance = new LatencyNumberConformance(distributor, sla, 
						query, sla.getMetric().getValue());
			} else if (sla.getScope() instanceof Single) {
				conformance = new LatencySingleConformance(distributor, sla, 
						query);
			} else if (sla.getScope() instanceof Rate) {
				conformance = new LatencyRateConformance(distributor, sla, 
						query, sla.getMetric().getValue());
			}
		}

		return conformance;
	}

}
