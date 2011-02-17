/** Copyright [2011] [The Odysseus Team]
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
