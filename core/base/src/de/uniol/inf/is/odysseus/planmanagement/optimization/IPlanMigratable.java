package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

/**
 * Defines an object which provides informations for plan migrations.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanMigratable {
	/**
	 * Returns the current execution plan.
	 * 
	 * @return the current execution plan.
	 */
	public IExecutionPlan getEditableExecutionPlan();

	/**
	 * Returns the current selected {@link ISchedulerManager}.
	 * 
	 * @return the current selected {@link ISchedulerManager}.
	 */
	public ISchedulerManager getSchedulerManager();
}
