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
package de.uniol.inf.is.odysseus.brokerconfig;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.rules.BrokerTransformationHelper;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

public class BrokerConfigurationBuilder implements IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();

	public BrokerConfigurationBuilder() {
		TransformationConfiguration cfg = new TransformationConfiguration(new BrokerTransformationHelper(), "relational",

		// ObjectTracking

				"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval", // ok
				
				"de.uniol.inf.is.odysseus.latency.ILatency"
				// ok
				);
		cfg.setOption("IBrokerInterval", true);
		settings.add(new ParameterTransformationConfiguration(cfg));

	}

	@Override
	public List<IQueryBuildSetting<?>> getConfiguration() {
		return settings;
	}
	
	@Override
	public String getName() {
		return "BrokerConfig";
	}

	// @Override
	// public ParameterTransformationConfiguration get() {
	// trafoConfigParam.getValue().setOption("IBrokerInterval", true);
	// return trafoConfigParam;
	// }

}
