package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.util.List;

public enum BenchmarkIdHolder {
	INSTANCE;

	private int lastId;

	private BenchmarkIdHolder() {
		lastId = 0;
	}

	public void calculateNextId(List<Benchmark> benchmarks) {
		if (benchmarks == null) {
			return;
		}

		for (Benchmark benchmark : benchmarks) {
			if (benchmark.getId() > lastId) {
				lastId = benchmark.getId();
			}
		}
	}

	public int generateNextId() {
		return ++lastId;
	}
	
	public int getLastId() {
		return lastId;
	}
}