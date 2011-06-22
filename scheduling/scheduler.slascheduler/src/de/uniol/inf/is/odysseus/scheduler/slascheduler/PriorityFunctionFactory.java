package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioAvg;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioMax;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.priority.PrioSum;

public class PriorityFunctionFactory {
	
	public static String MAX = "max";
	public static String SUM = "sum";
	public static String AVG = "avg";

	public IPriorityFunction buildPriorityFunction(String functionName) {
		if (MAX.equals(functionName)) {
			return new PrioMax();
		} else if (SUM.equals(functionName)) {
			return new PrioSum();
		} else if (AVG.equals(functionName)) {
			return new PrioAvg();
		} else {
			throw new RuntimeException("Unknown sla priority function id: " + functionName);
		}
	}
	
}
