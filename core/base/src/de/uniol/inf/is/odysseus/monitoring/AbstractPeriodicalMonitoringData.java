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

abstract public class AbstractPeriodicalMonitoringData<T> extends AbstractPublisher<T> implements IPeriodicalMonitoringData<T>{
	
	private IMonitoringDataProvider target;
	private String type;
	
	public AbstractPeriodicalMonitoringData(IMonitoringDataProvider target, String type) {
		super();
		this.target = target;
		this.type = type;
	}

	@Override
	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	@Override
	public abstract AbstractPeriodicalMonitoringData<T> clone();
	
	@Override
	public void cancelMonitoring() {
		synchronized (this.subscribers) {
			this.subscribers.clear();
		}
		MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(this);
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	final public String getType() {
		return type;
	}
}
