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
package de.uniol.inf.is.odysseus.benchmark.prio.physical;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.benchmark.result.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.core.server.monitoring.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.core.server.monitoring.IDescriptiveStatistics;
import de.uniol.inf.is.odysseus.interval_latency_priority.ILatencyPriority;

@Root(name = "priorityresult")
public class PriorityBenchmarkResult<T extends ILatencyPriority> extends
		AbstractBenchmarkResult<T> {
	
	private static final long serialVersionUID = -6833977068355934158L;
	@Element(name="prioritizedStatistics")
	private DescriptiveStatistics prioritizedStats = new DescriptiveStatistics();

	/**
	 * @param descStats
	 */
	public PriorityBenchmarkResult(IDescriptiveStatistics descStats) {
		super.setStatistics(descStats);
	}

	@Override
	public void add(T object) {
		if (object.getPriority() == 0) {
			super.add(object);
			getStatistics().addValue(object.getLatency());
		} else {
			++size;
			prioritizedStats.addValue(object.getLatency());
		}
	}

	@Override
	public String toString() {
		Persister p = new Persister();
		StringWriter writer = new StringWriter();
		try {
			p.write(this, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	
	@Override
	public PriorityBenchmarkResult<T> clone() {
		return new PriorityBenchmarkResult<T>(getStatistics());
	}
	
}
