package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencyNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.LatencySingleConformance;
import de.uniol.inf.is.odysseus.sla.SLA;
import de.uniol.inf.is.odysseus.sla.metric.Latency;
import de.uniol.inf.is.odysseus.sla.scope.Average;
import de.uniol.inf.is.odysseus.sla.scope.Number;
import de.uniol.inf.is.odysseus.sla.scope.Single;

/**
 * Factory for building sla conformance objects
 * 
 * @author Thomas Vogelgesang
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class SLAConformanceFactory {

	/**
	 * creates a new SLAConformance object for a given SLA
	 * 
	 * @param sla
	 * @return a new SLAConformance object or null, if no matching
	 *         SLAConformance object for the given sla exists
	 */
	public ISLAConformance createSLAConformance(SLA sla, 
			ISLAViolationEventDistributor distributor, IQuery query) {
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
			}
		}

		return conformance;
	}

}
