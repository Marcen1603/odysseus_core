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
package de.uniol.inf.is.odysseus.rcp.viewer.objectfusionconfig;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;


public class ObjectFusionQueryBuildConfiguration extends AbstractQueryBuildConfiguration {
		
	public ObjectFusionQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						new BrokerTransformationHelper(),
						"relational",
						ITimeInterval.class.getName(),
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey",
						"de.uniol.inf.is.odysseus.latency.ILatency",
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability", 
						"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval",
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime")));
	}
	
	public ObjectFusionQueryBuildConfiguration(
			List<IQueryBuildSetting<?>> settings) {
		settings.addAll(settings);
	}

	@Override
	public String getName() {
		return "ObjectFusion";
	}
	
	@Override
	public IQueryBuildConfiguration clone() {
		return new ObjectFusionQueryBuildConfiguration(settings);
	}

}
