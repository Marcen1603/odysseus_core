package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBenchmarkerExecution implements IBenchmarkerExecution{

	protected List<Long> executionTimes = new ArrayList<Long>();
	protected Integer numberOfElements;
	protected int numberOfExecutions;
	
	public abstract String toString();
	
	@Override
	public long getAverageExecutionTime(long maximumExecutionTime) {
		long sumExecutionTime = 0l;
		
		for (Long executionTime : executionTimes) {
			if (executionTime == -1l){
				return -1l;
			} else if (executionTime == 0l){
				return 0l;
			}  else if (executionTime >= maximumExecutionTime){
				return maximumExecutionTime;
			} else {
				sumExecutionTime += executionTime;				
			}
		}
		return sumExecutionTime / executionTimes.size();
	}

	@Override
	public void addExecutionTime(long executionTime) {
		this.executionTimes.add(executionTime);
	}
}
