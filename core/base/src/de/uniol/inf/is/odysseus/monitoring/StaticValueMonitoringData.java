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
package de.uniol.inf.is.odysseus.monitoring;

/**
 * @author Jonas Jacobi
 */
public class StaticValueMonitoringData<T> implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	private T value;
	private String type;

	public StaticValueMonitoringData(IMonitoringDataProvider target, String type, T value) {
		this.target = target;
		this.value = value;
		this.type = type;
	}
	public StaticValueMonitoringData(
			StaticValueMonitoringData<T> staticValueMonitoringData) {
		this.target = staticValueMonitoringData.target;
		this.value = staticValueMonitoringData.value;
		this.type = staticValueMonitoringData.type;
	}
	@Override
	public IMonitoringDataProvider getTarget() {
		return this.target;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@Override
	public StaticValueMonitoringData<T> clone() {
		return new StaticValueMonitoringData<T>(this);
	}
	
	@Override
	public void reset() {
		// Value does not change, so not reset necessary!	
	}
	
	@Override
	public void cancelMonitoring() {
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
}
