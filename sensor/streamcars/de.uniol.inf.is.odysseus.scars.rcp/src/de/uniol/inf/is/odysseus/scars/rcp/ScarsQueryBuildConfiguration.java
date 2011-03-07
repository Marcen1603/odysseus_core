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
package de.uniol.inf.is.odysseus.scars.rcp;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;

public class ScarsQueryBuildConfiguration implements IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();

	public ScarsQueryBuildConfiguration() {
		TransformationConfiguration cfg = new TransformationConfiguration(
				new BrokerTransformationHelper(),
				"relational",

				// ObjectTracking
				"de.uniol.inf.is.odysseus.metadata.ITimeInterval",
				"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey",
				"de.uniol.inf.is.odysseus.metadata.ILatency",  // ok
				"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability",
				"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime",
		
				// StreamCars
				"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer",
				"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain",
				"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency");


		cfg.setOption("IBrokerInterval", true);
		settings.add(new ParameterTransformationConfiguration(cfg));

	}

	@Override
	public List<IQueryBuildSetting<?>> getConfiguration() {
		return settings;
	}

	@Override
	public String getName() {
		return "StreamCars";
	}
	
//	@Override
//	public ParameterTransformationConfiguration get() {
//		trafoConfigParam.getValue().setOption("IBrokerInterval", true);
//		return trafoConfigParam;
//	}

}
