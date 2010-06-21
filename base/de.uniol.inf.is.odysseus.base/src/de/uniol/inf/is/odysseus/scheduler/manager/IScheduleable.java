package de.uniol.inf.is.odysseus.scheduler.manager;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;

/**
 * Describes an object which provides informations that are relevant for the
 * scheduling module.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IScheduleable {
	/**
	 * Returns the execution plan which should be scheduled.
	 * 
	 * @return The execution plan which should be scheduled.
	 */
	public IExecutionPlan getExecutionPlan();
}
