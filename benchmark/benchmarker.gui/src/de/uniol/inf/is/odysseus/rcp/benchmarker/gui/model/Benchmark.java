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

public class Benchmark {

	private BenchmarkParam param;
	private List<BenchmarkResult> results;
	private BenchmarkMetadata metadata;
	
	private transient BenchmarkGroup parentGroup;

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
	
	public BenchmarkGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(BenchmarkGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public boolean hasResults() {
		if (results.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	///

	public void setResults(List<BenchmarkResult> results) {
		this.results = results;
	}

	public void setMetadata(BenchmarkMetadata metadata) {
		this.metadata = metadata;
	}
	
	
	
	
	
	
	
}
