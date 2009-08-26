package de.uniol.inf.is.odysseus.scheduler.manager;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;

public interface IScheduleable {
	public IExecutionPlan getExecutionPlan();
}
