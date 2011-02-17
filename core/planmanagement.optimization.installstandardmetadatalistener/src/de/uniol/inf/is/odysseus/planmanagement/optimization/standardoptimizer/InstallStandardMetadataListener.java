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

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.PlanMonitor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class InstallStandardMetadataListener implements IPostOptimizationAction {

	// TODO: Konfigurierbar machen
	private static long MONITORING_PERIOD = 30000;

	@Override
	public void run(IQuery query, OptimizationConfiguration parameter) {
		query.addPlanMonitor(MonitoringDataTypes.DATARATE.name,
				new PlanMonitor(query, false,false,
						MonitoringDataTypes.DATARATE.name,
						MONITORING_PERIOD));
		query.addPlanMonitor(MonitoringDataTypes.SELECTIVITY.name,
				new PlanMonitor(query, false,false,
						MonitoringDataTypes.SELECTIVITY.name, -1));
	}

}
