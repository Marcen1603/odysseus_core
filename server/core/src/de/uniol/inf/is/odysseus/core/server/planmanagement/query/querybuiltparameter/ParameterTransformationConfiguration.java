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
package de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;

/**
 * {@link IQueryBuildSetting} which provides a a
 * {@link TransformationConfiguration} for the query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterTransformationConfiguration extends
		Setting<TransformationConfiguration> implements IQueryBuildSetting<TransformationConfiguration> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4435801645319473034L;

	/**
	 * Creates a ParameterTransformationConfiguration.
	 * 
	 * @param value
	 *            a {@link TransformationConfiguration} for the query.
	 */
	public ParameterTransformationConfiguration(
			TransformationConfiguration value) {
		super(value);
	}

	public ParameterTransformationConfiguration(
			ParameterTransformationConfiguration setting) {
		super(new TransformationConfiguration(setting.getValue()));
	}
}
