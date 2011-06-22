package de.uniol.inf.is.odysseus.scheduler.slascheduler.placement;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformancePlacement;

public class LatencySLAConformancePlacement implements ISLAConformancePlacement {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void placeSLAConformance(IPartialPlan plan,
			ISLAConformance conformance) {
		// it is expected that there is only one query per partial plan!
		// TODO: generalization: 1 pp == n queries?
		IPhysicalOperator root = plan.getQueryRoots().get(0);
		if (root instanceof ISource) {
			ISubscribable subscribable = (ISubscribable)root;
			subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
		} else {
			throw new RuntimeException("Cannot connect SLA conformance operator to query root");
		}
	}

}
