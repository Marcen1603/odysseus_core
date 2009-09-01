package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

public interface IPlanMigratable {
	public IEditableExecutionPlan getEditableExecutionPlan();
	
	public ISchedulerManager getSchedulerManager();
}
