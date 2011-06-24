package de.uniol.inf.is.odysseus.scheduler.slascheduler;

/**
 * Interface for priority functions
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface IPriorityFunction {
	/**
	 * calculates the priority of a partial plan from the given cost values
	 * 
	 * @param cost
	 *            cost values calculated by cost functions and starvation
	 *            freedom functions
	 * @return the absolute priority of a partial plan
	 */
	public double calcPriority(double... cost);

}
