package de.uniol.inf.is.odysseus.action.benchmark;


public interface IActuatorBenchmark {
	
	public enum Operation{DATAEXTRACTION, QUERYPROCESSING, ACTIONEXECTION};
	
	public void addBenchmarkData(BenchmarkData data);
		
}
