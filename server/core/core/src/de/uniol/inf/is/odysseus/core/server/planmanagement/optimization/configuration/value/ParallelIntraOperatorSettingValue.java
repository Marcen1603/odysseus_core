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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for intra operator parallelization, used in transformations to
 * created threaded versions of physical operators, possible contains individual
 * settings for specific operators
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelIntraOperatorSettingValue {
	private int globalDegree = 0;
	private int globalBuffersize = 0;

	// maps unique operator id to individual degree
	private Map<String, ParallelIntraOperatorSettingValueElement> individualSettings = new HashMap<String, ParallelIntraOperatorSettingValueElement>();

	public ParallelIntraOperatorSettingValue() {
	}

	public ParallelIntraOperatorSettingValue(int globalDegree,
			int globalBuffersize) {
		this.globalDegree = globalDegree;
		this.globalBuffersize = globalBuffersize;
	}

	public int getGlobalDegree() {
		return globalDegree;
	}

	public void setGlobalDegree(int globalDegree) {
		this.globalDegree = globalDegree;
	}

	public boolean hasIndividualSettings() {
		return !individualSettings.isEmpty();
	}

	public boolean hasIndividualSettingsForOperator(String operatorId) {
		if (operatorId == null) {
			return false;
		}
		return individualSettings.containsKey(operatorId);
	}

	public ParallelIntraOperatorSettingValueElement getIndividualSettings(
			String operatorId) {
		if (operatorId == null) {
			return null;
		}
		return individualSettings.get(operatorId);
	}

	public void addIndividualSettings(String operatorId,
			ParallelIntraOperatorSettingValueElement individualSetting) {
		if (individualSettings.containsKey(operatorId)) {
			throw new IllegalArgumentException(
					"Duplicate definition for operator with id " + operatorId);
		}
		individualSettings.put(operatorId, individualSetting);
	}
	
	public void removeIndividualSettings(String operatorId) {
		this.individualSettings.remove(operatorId);
	}

	public int getGlobalBuffersize() {
		return globalBuffersize;
	}

	public void setGlobalBuffersize(int globalBuffersize) {
		this.globalBuffersize = globalBuffersize;
	}

}
