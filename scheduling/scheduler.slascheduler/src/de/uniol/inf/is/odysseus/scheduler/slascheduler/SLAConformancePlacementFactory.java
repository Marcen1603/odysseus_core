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
import de.uniol.inf.is.odysseus.scheduler.slascheduler.placement.LatencySLAConformancePlacement;

/**
 * Factory for building strategies to place sla conformance operators in
 * physical plan 
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAConformancePlacementFactory {

	/**
	 * builds a new object implementing {@link ISLAConformancePlacement}
	 * according to the given sla
	 * 
	 * @param sla the sla
	 * @return a new strategy object for placing sla conformance objects
	 * 
	 * @throws RuntimeException if no matching strategy could be created
	 */
	public ISLAConformancePlacement buildSLAConformancePlacement(SLA sla) {
		if (sla.getMetric() instanceof Latency) {
			return new LatencySLAConformancePlacement();
		}
        throw new RuntimeException(
        		"no placement for sla conformance operator "
        				+ sla.getMetric().getClass());
	}

}
