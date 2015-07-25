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
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelInterOperatorSetting extends Setting<Map<String, ParallelOperatorConfiguration>> implements IQueryBuildSetting<Map<String, ParallelOperatorConfiguration>>{

	protected ParallelInterOperatorSetting(
			Map<String, ParallelOperatorConfiguration> value) {
		super(value);
	}

	public ParallelInterOperatorSetting() {
		super(new HashMap<String, ParallelOperatorConfiguration>());
	}

	public ParallelOperatorConfiguration getSettingsForOperator(String operatorId){
		if (super.getValue().containsKey(operatorId.toLowerCase())){
			return super.getValue().get(operatorId.toLowerCase());
		}
		return null;
	}
	
	public boolean settingsForOperatorExists(String operatorId) {
		
		return super.getValue().containsKey(operatorId.toLowerCase());
	}

	public void addSettingsForOperator(String operatorId, ParallelOperatorConfiguration settings){
		if (!settingsForOperatorExists(operatorId)){
			super.getValue().put(operatorId.toLowerCase(), settings);			
		} else {
			throw new IllegalArgumentException("Multiple operator settings for id: "+operatorId);
		}
	}
	
	public void addSettingsForOperators(List<String> operatorIds, ParallelOperatorConfiguration settings){
		for (String operatorId : operatorIds) {
			addSettingsForOperator(operatorId, settings);
		}
	}

	public List<String> getOperatorIds() {
		List<String> operatorIds = new ArrayList<String>();
		operatorIds.addAll(super.getValue().keySet());
		return operatorIds;
	}
}
