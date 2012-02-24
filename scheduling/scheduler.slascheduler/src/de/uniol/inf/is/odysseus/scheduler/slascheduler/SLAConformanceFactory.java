package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Latency;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Average;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Number;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Rate;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Single;
import de.uniol.inf.is.odysseus.core.sla.SLA;
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
