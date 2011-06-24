package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioAvg;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioMax;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioSum;

/**
 * Factory for building Priority functions
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PriorityFunctionFactory {

	/**
	 * id for maximum priority function
	 */
	public static String MAX = "max";
	/**
	 * id for sum priority function
	 */
	public static String SUM = "sum";
	/**
	 * id for average priority function
	 */
	public static String AVG = "avg";

	/**
	 * returns a new priority function according to the given function name
	 * @param functionName the name of the funczion to be built
	 * @return the new priority function matching the given function name
	 * @throws {@link RuntimeException} if an unknown function name is given
	 */
	public IPriorityFunction buildPriorityFunction(String functionName) {
		if (MAX.equals(functionName)) {
			return new PrioMax();
		} else if (SUM.equals(functionName)) {
			return new PrioSum();
		} else if (AVG.equals(functionName)) {
			return new PrioAvg();
		} else {
			throw new RuntimeException("Unknown sla priority function id: "
					+ functionName);
		}
	}

}
