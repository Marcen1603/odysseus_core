package de.uniol.inf.is.odysseus.generator;

public interface IProviderRunner {

	double getLastThroughput();

	void printStats();

	void setDelay(long sleep);

}
