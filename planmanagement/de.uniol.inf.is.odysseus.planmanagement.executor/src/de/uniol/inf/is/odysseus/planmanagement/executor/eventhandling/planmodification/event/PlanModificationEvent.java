package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

public class PlanModificationEvent extends
		AbstractPlanModificationEvent<IPlan> {

	public static String PLAN_REOPTIMIZE = "PLAN_REOPTIMIZE";

	public PlanModificationEvent(IPlanManager sender, String id,
			IPlan value) {
		super(sender, id, value);
	}
}
