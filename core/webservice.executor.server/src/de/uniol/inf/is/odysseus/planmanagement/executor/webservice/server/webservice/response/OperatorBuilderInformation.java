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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.Set;

/**
 * Information representing a OperatorBuilderFactory
 * 
 * @author Merlin Wasmann
 *
 */
public class OperatorBuilderInformation {
	private Set<ParameterInfo> parameters;
	
	private int minInputOperatorCount;
	private int maxInputOperatorCount;
	
	private String name;

	public Set<ParameterInfo> getParameters() {
		return parameters;
	}

	public void setParameters(Set<ParameterInfo> parameters) {
		this.parameters = parameters;
	}

	public int getMinInputOperatorCount() {
		return minInputOperatorCount;
	}

	public void setMinInputOperatorCount(int minInputOperatorCount) {
		this.minInputOperatorCount = minInputOperatorCount;
	}

	public int getMaxInputOperatorCount() {
		return maxInputOperatorCount;
	}

	public void setMaxInputOperatorCount(int maxInputOperatorCount) {
		this.maxInputOperatorCount = maxInputOperatorCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
