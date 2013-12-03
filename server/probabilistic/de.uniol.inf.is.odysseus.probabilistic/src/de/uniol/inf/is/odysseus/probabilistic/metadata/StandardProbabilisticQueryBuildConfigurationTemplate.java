/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

/**
 * Standard query build configuration for probabilistic data streams supporting Rewrite, Query Sharing and Reconstruction.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class StandardProbabilisticQueryBuildConfigurationTemplate extends AbstractQueryBuildConfigurationTemplate {
	/**
	 * Default constructor.
	 */
	public StandardProbabilisticQueryBuildConfigurationTemplate() {
		this.settings.add(new ParameterTransformationConfiguration(new TransformationConfiguration(ITimeInterval.class, IProbabilistic.class, ITimeIntervalProbabilistic.class)));
		this.settings.add(ParameterDoRewrite.TRUE);
		this.settings.add(ParameterPerformQuerySharing.TRUE);
		this.settings.add(ParameterShareSimilarOperators.FALSE);
	}

	/**
	 * Creates a new {@link StandardProbabilisticQueryBuildConfigurationTemplate} with the given settings.
	 * 
	 * @param settings
	 *            The query build settings
	 */
	public StandardProbabilisticQueryBuildConfigurationTemplate(final List<IQueryBuildSetting<?>> settings) {
		this.settings.addAll(settings);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate#getName()
	 */
	@Override
	public final String getName() {
		return "StandardProbabilistic";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfiguration#clone()
	 */
	@Override
	public final IQueryBuildConfigurationTemplate clone() {
		return new StandardProbabilisticQueryBuildConfigurationTemplate(this.settings);
	}

}
