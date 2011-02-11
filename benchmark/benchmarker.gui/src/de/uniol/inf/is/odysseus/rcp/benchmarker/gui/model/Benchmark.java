package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {

	private BenchmarkParam param;
	private List<BenchmarkResult> results;
	private BenchmarkMetadata metadata;

	public Benchmark() {
		super();
		this.results = new ArrayList<BenchmarkResult>();
	}

	public Benchmark(BenchmarkParam param) {
		this();
		this.param = param;
	}
	
	public int getId() {
		return param.getId();
	}
	
	public String getName() {
		return param.getName();
	}

	public List<BenchmarkResult> getResults() {
		return results;
	}

	public BenchmarkParam getParam() {
		return param;
	}

	public void setParam(BenchmarkParam param) {
		this.param = param;
	}

	public BenchmarkMetadata getMetadata() {
		return metadata;
	}

	public void setMetada(BenchmarkMetadata metadata) {
		this.metadata = metadata;
	}
}
