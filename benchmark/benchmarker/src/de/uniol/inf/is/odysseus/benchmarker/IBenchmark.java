package de.uniol.inf.is.odysseus.benchmarker;

import java.util.Collection;

public interface IBenchmark {
	public void setSchedulingStrategy(String schedStrat);

	public void setScheduler(String scheduler);

	public void setBufferPlacementStrategy(String bufferPlacement);

	public void addQuery(String language, String query);

	public void setDataType(String dataType);

	public void setMetadataTypes(String... types);

	public void setOption(String name, Object value);

	public void setResultFactory(String className);

	public void setMaxResults(long maxresults);

	public String[] getMetadataTypes();

	public <T> Collection<IBenchmarkResult<T>> runBenchmark() throws BenchmarkException;

	public void setUsePunctuations(boolean b);

	public void setUseLoadShedding(boolean b);

	public void setBenchmarkMemUsage(boolean b);

	public DescriptiveStatistics getMemUsageStatistics();

	public void setExtendedPostPriorisation(boolean b);

	public void setNoMetadataCreation(boolean b);

	public void setResultPerQuery(boolean b);
}
