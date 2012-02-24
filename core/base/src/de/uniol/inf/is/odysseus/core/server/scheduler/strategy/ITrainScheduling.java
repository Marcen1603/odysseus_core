package de.uniol.inf.is.odysseus.core.server.scheduler.strategy;

public interface ITrainScheduling {

	public boolean schedule(long maxTime, int trainSize);
	
}
