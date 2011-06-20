package de.uniol.inf.is.odysseus.scheduler.slascheduler;


public interface IPriorityFunction {
	
	public double calcPriority(double... cost);

}
