/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.interoperator.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Build setting for inter operator parallelization. these setting is used for
 * operator custom configurations. The setting is created in #INTEROPERATOR
 * keyword
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelInterOperatorSetting extends
		Setting<Map<String, ParallelOperatorConfiguration>> implements
		IQueryBuildSetting<Map<String, ParallelOperatorConfiguration>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2749552353246414512L;

	protected ParallelInterOperatorSetting(
			Map<String, ParallelOperatorConfiguration> value) {
		super(value);
	}

	public ParallelInterOperatorSetting() {
		super(new HashMap<String, ParallelOperatorConfiguration>());
	}

	/**
	 * returns the custom configuration for a specific operator id
	 * 
	 * @param operatorId
	 * @return
	 */
	public ParallelOperatorConfiguration getConfigurationForOperator(
			String operatorId) {
		if (super.getValue().containsKey(operatorId.toLowerCase())) {
			return super.getValue().get(operatorId.toLowerCase());
		}
		return null;
	}

	/**
	 * checks if custom configuration for a specific operator exists
	 * 
	 * @param operatorId
	 * @return
	 */
	public boolean configurationForOperatorExists(String operatorId) {
		return super.getValue().containsKey(operatorId.toLowerCase());
	}

	/**
	 * adds a new configuration for a specific operator id, throws error if
	 * definition is mutliple for the same operator id
	 * 
	 * @param operatorId
	 * @param settings
	 */
	public void addConfigurationForOperator(String operatorId,
			ParallelOperatorConfiguration settings) {
		if (!configurationForOperatorExists(operatorId)) {
			super.getValue().put(operatorId.toLowerCase(), settings);
		} else {
			throw new IllegalArgumentException(
					"Multiple operator settings for id: " + operatorId);
		}
	}

	/**
	 * adds same configuration for multiple operator ids
	 * 
	 * @param operatorIds
	 * @param configuration
	 */
	public void addConfigurationForOperators(List<String> operatorIds,
			ParallelOperatorConfiguration configuration) {
		for (String operatorId : operatorIds) {
			addConfigurationForOperator(operatorId, configuration);
		}
	}

	/**
	 * returns a list of operator ids which has specific configuration
	 * 
	 * @return list of operatorIDs
	 */
	public List<String> getOperatorIds() {
		List<String> operatorIds = new ArrayList<String>();
		operatorIds.addAll(super.getValue().keySet());
		return operatorIds;
	}
	
	/**
	 * removes the specific configuration of an operator
	 * 
	 * @param operatorId
	 */
	public void removeConfigurationForOperator(String operatorId) {
		if (configurationForOperatorExists(operatorId)) {
			super.getValue().remove(operatorId.toLowerCase());
		}
	}
}
