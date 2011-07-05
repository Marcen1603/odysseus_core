package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public interface IQuerySharing {
	/**
	 * @return the best plan with respect to cost-based priority and query
	 *         sharing effort
	 */
	public IScheduling getNextPlan();

	/**
	 * sets the data required for calculating the query sharing effort
	 */
	public void setPriority(IScheduling plan, double priority);
	
	/**
	 * updates the data structure storing information about the effort of query
	 * sharing
	 */
	public void refreshEffortTable(List<IScheduling> plans);
	
}
