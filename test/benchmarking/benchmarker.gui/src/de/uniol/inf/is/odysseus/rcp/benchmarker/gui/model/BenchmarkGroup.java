/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse enthält die Daten für eine BenchmarkGroup
 * @author Stefanie Witzke
 *
 */
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
