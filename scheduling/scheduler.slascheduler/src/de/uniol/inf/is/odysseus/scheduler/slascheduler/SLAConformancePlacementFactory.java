package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.placement.LatencySLAConformancePlacement;
import de.uniol.inf.is.odysseus.sla.SLA;
import de.uniol.inf.is.odysseus.sla.metric.Latency;

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
		} else {
			throw new RuntimeException(
					"no placement for sla conformance operator "
							+ sla.getMetric().getClass());
		}
	}

}
