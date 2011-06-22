package de.uniol.inf.is.odysseus.scheduler.strategy;

public interface ITrainScheduling {

	public boolean schedule(long maxTime, int trainSize);
	
}
