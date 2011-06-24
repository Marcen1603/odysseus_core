package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Average;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Single;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencySingleConformance;

/**
 * Factory for building sla conformance objects
 * 
 * @author Thomas Vogelgesang
 *
 */
public class SLAConformanceFactory {

	/**
	 * creates a new SLAConformance object for a given SLA
	 * 
	 * @param sla
	 * @return a new SLAConformance object or null, if no matching
	 *         SLAConformance object for the given sla exists
	 */
	public ISLAConformance createSLAConformance(SLA sla, 
			ISLAViolationEventDistributor distributor, IPartialPlan plan) {
		ISLAConformance conformance = null;

		if (sla.getMetric() instanceof Latency) {
			if (sla.getScope() instanceof Average) {
				conformance = new LatencyAverageConformance(distributor, sla, 
						plan);
			} else if (sla.getScope() instanceof Number) {
				conformance = new LatencyNumberConformance(distributor, sla, 
						plan, (Long) sla.getMetric().getValue());
			} else if (sla.getScope() instanceof Single) {
				conformance = new LatencySingleConformance(distributor, sla, 
						plan);
			}
		}

		return conformance;
	}

}
