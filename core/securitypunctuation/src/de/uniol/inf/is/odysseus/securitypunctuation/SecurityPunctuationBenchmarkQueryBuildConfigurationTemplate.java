/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.securitypunctuation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

public class SecurityPunctuationBenchmarkQueryBuildConfigurationTemplate extends
		AbstractQueryBuildConfigurationTemplate {

	public SecurityPunctuationBenchmarkQueryBuildConfigurationTemplate() {
		TransformationConfiguration transformationConfiguration = new TransformationConfiguration(
				"relational", 
				ITimeInterval.class, ILatency.class);
//		transformationConfiguration.setOption("isSecurityAware", true);
		settings.add(new ParameterTransformationConfiguration(transformationConfiguration));
		
		settings.add(ParameterDoRewrite.TRUE);
		settings.add(ParameterPerformQuerySharing.TRUE);
		settings.add(ParameterShareSimilarOperators.FALSE);
	}
	
	public SecurityPunctuationBenchmarkQueryBuildConfigurationTemplate(List<IQueryBuildSetting<?>> settings) {
		settings.addAll(settings);
	}

	@Override
	public String getName() {
		return "SecurityPunctuationBenchmark";
	}
	
	@Override
	public IQueryBuildConfigurationTemplate clone() {		
		return new SecurityPunctuationLatencyQueryBuildConfigurationTemplate(settings);
	}

}
