package de.uniol.inf.is.odysseus.action.benchmark;




public interface IActuatorBenchmark {
	
	public enum Operation{QUERYPROCESSING, ACTIONEXECTION};
	
	public void addBenchmarkData(BenchmarkData data);
	
	public void addQuery(String query, String lang);
	
	public void run();
	
	public void stop();
		
}
