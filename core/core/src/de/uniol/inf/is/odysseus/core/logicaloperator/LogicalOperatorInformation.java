/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.logicaloperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DGeesen
 *
 */
public class LogicalOperatorInformation {
	
	private String operatorName;
	private String doc;
	private List<LogicalParameterInformation> parameters = new ArrayList<>();
	private int maxPorts;
	private int minPorts;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public List<LogicalParameterInformation> getParameters() {
		return parameters;
	}

	public void setParameters(List<LogicalParameterInformation> parameters) {
		this.parameters = parameters;
	}

	public int getMinPorts() {
		return minPorts;
	}

	public void setMinPorts(int minPorts) {
		this.minPorts = minPorts;
	}

	public int getMaxPorts() {
		return maxPorts;
	}

	public void setMaxPorts(int maxPorts) {
		this.maxPorts = maxPorts;
	}	

}
