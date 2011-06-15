package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Average;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Single;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencySingleConformance;

public class SLAConformanceFactory {

	/**
	 * creates a new SLAConformance object for a given SLA
	 * 
	 * @param sla
	 * @return a new SLAConformance object or null, if no matching
	 *         SLAConformance object for the given sla exists
	 */
	public ISLAConformance createSLAConformance(SLA sla) {
		ISLAConformance conformance = null;

		if (sla.getMetric() instanceof Latency) {
			if (sla.getScope() instanceof Average) {
				conformance = new LatencyAverageConformance();
			} else if (sla.getScope() instanceof Number) {
				conformance = new LatencyNumberConformance((Long) sla.getMetric().getValue());
			} else if (sla.getScope() instanceof Single) {
				conformance = new LatencySingleConformance();
			}
		}

		return conformance;
	}

}
