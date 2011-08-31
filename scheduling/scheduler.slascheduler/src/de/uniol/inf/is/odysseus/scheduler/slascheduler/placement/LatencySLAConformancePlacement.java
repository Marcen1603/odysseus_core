package de.uniol.inf.is.odysseus.scheduler.slascheduler.placement;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLaConformance;

/**
 * Placement strategy for latency based sla conformance operators
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class LatencySLAConformancePlacement implements ISLAConformancePlacement {

	/**
	 * places the given sla conformance operator at the root of the given
	 * partial plan, assuming that one partial plan consists only of one query
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ISubscribable<?, ?> placeSLAConformance(IQuery query,
			ISLAConformance conformance) {
		// it is expected that there is only one query per partial plan!
		IPhysicalOperator root = query.getRoots().get(0);
		if (root instanceof ISource) {
			LatencyCalculationPipe<?> latencyCalc = new LatencyCalculationPipe();
			ISubscribable subscribable = (ISubscribable)latencyCalc;
			subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
			
			subscribable = (ISubscribable) root;
			subscribable.connectSink(latencyCalc, 0, 0, root.getOutputSchema());
			return subscribable;
		} else {
			throw new RuntimeException(
					"Cannot connect SLA conformance operator to query root: " + root);
		}
	}

	/**
	 * removes the given sla conformance operator from the related partial plan
	 * by disconnecting from the root operator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void removeSLAConformance(ISubscribable connectionPoint,
			ISLAConformance conformance) {
		connectionPoint.disconnectSink(conformance, 0, 0,
				((AbstractSLaConformance<?>) conformance).getOutputSchema());
	}
	
}
