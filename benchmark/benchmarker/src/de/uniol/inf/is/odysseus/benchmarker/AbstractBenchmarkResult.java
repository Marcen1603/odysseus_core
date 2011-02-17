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
	@Element
	private int queryId;
	@SuppressWarnings("unused")
	@Element
	private long duration;
	@Element(name = "statistics")
	private DescriptiveStatistics desc = new DescriptiveStatistics();

	@Override
	public void setStartTime(long start) {
		this.startTime = start;
	}

	@Override
	public void setEndTime(long end) {
		this.endTime = end;
	}

	@SuppressWarnings("unused")
	@Persist
	private void setDuration() {
		this.duration = this.getDuration();
	}

	@Override
	public long getDuration() {
		return this.endTime - this.startTime;
	}

	@Override
	public void add(T object) {
		++size;
	}

	@Override
	public long size() {
		return size;
	}

	public int getQueryId() {
		return queryId;
	}
	
	@Override
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	@Override
	public DescriptiveStatistics getStatistics() {
		return this.desc;
	}

}
