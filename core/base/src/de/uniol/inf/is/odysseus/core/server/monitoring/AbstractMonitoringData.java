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
package de.uniol.inf.is.odysseus.core.server.monitoring;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;

public abstract class AbstractMonitoringData<T> 
		implements IMonitoringData<T> {

	private IMonitoringDataProvider target;
	String type = null;
	
	public AbstractMonitoringData(String type){
		super();
		this.target = null;
		setType(type);
	}
	
	public AbstractMonitoringData(IMonitoringDataProvider target, String type) {
		super();
		this.target = target;
		this.type = type;
	}

	public AbstractMonitoringData(AbstractMonitoringData<T> other) {
		this.target = other.target;
		this.type = other.type;
	}

	@Override
	public IMonitoringDataProvider getTarget() {
		return target;
	}
	
	public void setTarget(IMonitoringDataProvider target) {
		this.target = target;
	}
	
	@Override
	public abstract AbstractMonitoringData<T> clone() ;

	@Override
	public void cancelMonitoring() {
	}
	
	@Override
	final public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "PlanMonitor " + type;
	}
}