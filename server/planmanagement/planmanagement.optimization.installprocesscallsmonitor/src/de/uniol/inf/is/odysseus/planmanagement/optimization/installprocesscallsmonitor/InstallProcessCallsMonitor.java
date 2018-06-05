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
package de.uniol.inf.is.odysseus.planmanagement.optimization.installprocesscallsmonitor;

import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.ProcessCallsMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class InstallProcessCallsMonitor implements IPostOptimizationAction {

	@Override
	public void run(IPhysicalQuery query, OptimizationConfiguration parameter, IExecutionPlan currentExecutionPlan) {
		query.addPlanMonitor("Root Monitor", new ProcessCallsMonitor(query,
				true,false, "Root Monitor",true));
		query.addPlanMonitor("Buffer Monitor", new ProcessCallsMonitor(query,
				false,true, "Buffer Monitor",true));

	}

}
