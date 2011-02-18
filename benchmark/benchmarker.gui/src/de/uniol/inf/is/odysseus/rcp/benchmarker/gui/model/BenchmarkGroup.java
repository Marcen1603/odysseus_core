package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkGroup {
	
	private final String groupName;

	private final List<Benchmark> benchmarks;
	
	private int currentId;

	public BenchmarkGroup(String name) {
		this.groupName = name;
		benchmarks = new ArrayList<Benchmark>();
		currentId = 0;
	}
	
	public String getName() {
		return groupName;
	}

	public List<Benchmark> getBenchmarks() {
		return benchmarks;
	}

	public boolean contains(int benchmarkId) {
		if (getBenchmark(benchmarkId) != null) {
			return true;
		}

		return false;
	}
	
	public void addBenchmark(Benchmark benchmark) {
		benchmarks.add(benchmark);
	}

	public Benchmark getBenchmark(int benchmarkId) {
		for (Benchmark bench : benchmarks) {
			if (benchmarkId == bench.getId()) {
				return bench;
			}
		}
		return null;
	}
	
	public int getNextId() {
		return ++currentId;
	}
	
	public void recalculateNextId() {
		for (Benchmark bench : benchmarks) {
			if (bench.getId() > currentId) {
				currentId = bench.getId();
			}
		}
	}
}
