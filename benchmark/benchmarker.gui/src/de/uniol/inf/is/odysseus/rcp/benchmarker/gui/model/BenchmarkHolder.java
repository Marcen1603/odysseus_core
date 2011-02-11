package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.util.ArrayList;
import java.util.List;

public enum BenchmarkHolder {

	INSTANCE;

	private final List<Benchmark> benchmarks;

	BenchmarkHolder() {
		benchmarks = new ArrayList<Benchmark>();
	}

	public List<Benchmark> getBenchmarks() {
		return benchmarks;
	}

	public void addBenchmarkIfNotExists(Benchmark benchmark) {
		if (benchmark == null) {
			return;
		}
		if (benchmark.getId() <= 0) {
			benchmark.getParam().setId(BenchmarkIdHolder.INSTANCE.generateNextId());
		}
		if (!contains(benchmark.getId())) {
		  this.benchmarks.add(benchmark);
		} else {
			throw new RuntimeException("Benchmark with ID " + benchmark.getId() + " already exists!!");
		}
	}
	
	public boolean contains(int benchmarkId) {
		if (getBenchmark(benchmarkId) != null) {
			return true;
		}
		
		return false;
	}
	
	public Benchmark getBenchmark(int benchmarkId) {
		for (Benchmark bench : benchmarks) {
			if (benchmarkId == bench.getId()) {
				return bench;
			}
		}
		return null;
	}
}
