package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.placement.LatencySLAConformancePlacement;

public class SLAConformancePlacementFactory {
	
	public ISLAConformancePlacement buildSLAConformancePlacement(SLA sla) {
		if (sla.getMetric() instanceof Latency) {
			return new LatencySLAConformancePlacement();
		} else {
			throw new RuntimeException("no placement for sla conformance operator " + sla.getMetric().getClass());
		}
	}

}
