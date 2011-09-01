/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.metadata;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

public class MiningQueryBuildConfiguration implements IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();

	@SuppressWarnings("unchecked")
	public MiningQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(new TransformationConfiguration("relational", ITimeInterval.class, IMiningMetadata.class, ILatency.class)));
		settings.add(ParameterDoRewrite.TRUE);
		settings.add(ParameterPerformQuerySharing.TRUE);
		settings.add(ParameterShareSimilarOperators.FALSE);
	}

	@Override
	public List<IQueryBuildSetting<?>> getConfiguration() {
		return settings;
	}

	@Override
	public String getName() {
		return "Standard Mining";
	}

}
