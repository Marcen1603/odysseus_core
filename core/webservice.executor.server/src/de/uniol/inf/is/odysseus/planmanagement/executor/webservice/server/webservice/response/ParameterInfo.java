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


/**
 * ParameterInfo contains information about a IParameter
 * 
 * @author Merlin Wasmann
 * 
 */
public class ParameterInfo {
	public static enum REQUIREMENT {
		MANDATORY, OPTIONAL
	}

	private String name;
	private REQUIREMENT requirement;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public REQUIREMENT getRequirement() {
		return requirement;
	}

	public void setRequirement(REQUIREMENT requirement) {
		this.requirement = requirement;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
