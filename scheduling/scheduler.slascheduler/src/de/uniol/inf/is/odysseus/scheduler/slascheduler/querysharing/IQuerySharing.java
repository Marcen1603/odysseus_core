package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

/**
 * Interface for considering the effort of query sharing
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface IQuerySharing {
	/**
	 * @return the best plan with respect to cost-based priority and query
	 *         sharing effort
	 */
	public IScheduling getNextPlan();

	/**
	 * sets the priority for the given scheduling unit
	 * @param plan the scheduling unit
	 * @param priority the priority of the given scheduling unit
	 */
	public void setPriority(IScheduling plan, double priority);

	/**
	 * updates the data structure storing information about the effort of query
	 * sharing
	 * @param plans list of all plans managed by the scheduler
	 */
	public void refreshEffortTable(List<IScheduling> plans);

}
