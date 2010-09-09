package de.uniol.inf.is.odysseus.benchmarker;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persist;

/**
 * @author Jonas Jacobi
 */
@Root(name = "result")
public abstract class AbstractBenchmarkResult<T> implements IBenchmarkResult<T> {
	private long startTime;
	private long endTime;
	protected int size = 0;
	@SuppressWarnings("unused")
	@Element
	private long duration;
	@Element(name = "statistics")
	private DescriptiveStatistics desc = new DescriptiveStatistics();

	public void setStartTime(long start) {
		this.startTime = start;
	}

	public void setEndTime(long end) {
		this.endTime = end;
	}

	@SuppressWarnings("unused")
	@Persist
	private void setDuration() {
		this.duration = this.getDuration();
	}

	public long getDuration() {
		return this.endTime - this.startTime;
	}

	public void add(T object) {
		++size;
	}

	public long size() {
		return size;
	}

	public DescriptiveStatistics getStatistics() {
		return this.desc;
	}

}
