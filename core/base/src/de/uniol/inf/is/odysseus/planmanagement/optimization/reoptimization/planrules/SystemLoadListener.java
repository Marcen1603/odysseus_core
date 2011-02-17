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
package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;
import de.uniol.inf.is.odysseus.planmanagement.plan.AbstractPlanReoptimizeRule;

/**
 * Default implementation of {@link ISystemMonitorListener}.
 * 
 * @author Tobias Witt
 *
 */
public class SystemLoadListener extends AbstractPlanReoptimizeRule implements ISystemMonitorListener {
	
	private double criticalCPULoad;
	private double criticalMemoryLoad;
	private ISystemMonitor systemMonitor;
	
	public SystemLoadListener(ISystemMonitor systemMonitor, double criticalCPULoad, double criticalMemoryLoad) {
		this.criticalCPULoad = criticalCPULoad;
		this.criticalMemoryLoad = criticalMemoryLoad;
		this.systemMonitor = systemMonitor;
		
		initialize();
	}
	
	public void initialize() {
		this.systemMonitor.addListener(this);
	}

	@Override
	public void deinitialize() {
		this.systemMonitor.removeListener(this);
	}

	@Override
	public void updateOccured() {
		double cpuLoad = this.systemMonitor.getAverageCPULoad();
		double memLoad = this.systemMonitor.getHeapMemoryUsage();
		//System.out.println("mem: "+memLoad);
		if (cpuLoad >= this.criticalCPULoad
				|| memLoad >= this.criticalMemoryLoad) {
			fireReoptimizeEvent();
		}
	}
	
}
