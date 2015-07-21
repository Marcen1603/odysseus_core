package de.uniol.inf.is.odysseus.parallelization.rcp.data;

public interface IBenchmarkerExecution {

	String getOdysseusScript();

	long getAverageExecutionTime(long maximumExecutionTime);

	void addExecutionTime(long executionTime);
}
