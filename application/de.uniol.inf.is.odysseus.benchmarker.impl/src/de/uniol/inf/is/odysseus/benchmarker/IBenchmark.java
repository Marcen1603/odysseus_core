package de.uniol.inf.is.odysseus.benchmarker;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;

public interface IBenchmark {

	public static class Configuration {
		public long maxResults;
		public String language;
		public String scheduler;
		public String schedulingStrategy;
		public String bufferPlacement;
		public AbstractQueryBuildParameter<?>[] buildParameters; 
		
		public Configuration() {
			this.bufferPlacement = "Standard Buffer Placement";
			this.scheduler = "Single Thread Scheduler";
			this.schedulingStrategy = "Round Robin (Iter)";
			this.maxResults = -1;
			this.buildParameters = null;
		}
	}

	public BenchmarkResult runBenchmark(String query, String language,
			Configuration config) throws BenchmarkException;

}
