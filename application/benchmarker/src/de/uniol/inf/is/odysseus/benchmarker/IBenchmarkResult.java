package de.uniol.inf.is.odysseus.benchmarker;

public interface IBenchmarkResult<T> {
	public void add(T object);

	public void setStartTime(long start);

	public void setEndTime(long start);

	public long getDuration();

	public long size();

	public DescriptiveStatistics getStatistics();
}
