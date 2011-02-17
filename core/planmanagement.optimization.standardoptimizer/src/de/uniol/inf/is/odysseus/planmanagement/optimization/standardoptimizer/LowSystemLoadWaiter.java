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
package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;

/**
 * Helper class, that informs the {@link StandardOptimizer}, when the system
 * load is below a certain limit.
 * 
 * @author Tobias Witt
 * 
 */
public class LowSystemLoadWaiter implements ISystemMonitorListener {
	
	private double lowCpuLoad;
	private double lowMemLoad;
	private ISystemMonitor systemMonitor;
	private StandardOptimizer optimizer;
	
	public LowSystemLoadWaiter(ISystemMonitor monitor, StandardOptimizer optimizer, double lowCpuLoad, double lowMemLoad) {
		this.systemMonitor = monitor;
		this.lowCpuLoad = lowCpuLoad;
		this.lowMemLoad = lowMemLoad;
		this.optimizer = optimizer;
		
		initialize();
	}
	
	private void initialize() {
		this.systemMonitor.addListener(this);
	}

	@Override
	public void updateOccured() {
		double mem = this.systemMonitor.getHeapMemoryUsage();
		double cpu = this.systemMonitor.getAverageCPULoad();
		//System.out.println("mem: "+mem);
		if (mem <= this.lowMemLoad && cpu <= this.lowCpuLoad) {
			this.systemMonitor.stop();
			this.optimizer.processPendingRequests();
		}
	}
	
}
