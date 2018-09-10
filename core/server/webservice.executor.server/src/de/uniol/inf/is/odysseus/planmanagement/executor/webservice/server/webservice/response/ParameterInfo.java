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
	private String value;
	private REQUIREMENT requirement;
	private boolean deprecated;
	private boolean mandatory;
	private String possibleValueMethod;
	private String doc;
	private String dataType;
	private ParameterInfo listDataType;
	private ParameterInfo mapKeyDataType;
	private ParameterInfo mapValueDataType;

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

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public boolean isMandatory(){
		return this.mandatory;
	}
	
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	public String getPossibleValueMethod() {
		return this.possibleValueMethod;
	}
	
	public void setPossibleValueMethod(String possibleValueMethod) {
		this.possibleValueMethod = possibleValueMethod;
	}
	
	public String getDoc() {
		return this.doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}
	
	public String getDataType() {
		return this.dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public ParameterInfo getListDataType() {
		return this.listDataType;
	}
	
	public void setListDataType(ParameterInfo listDataType) {
		this.listDataType = listDataType;
	}
	
	public ParameterInfo getMapKeyDataType() {
		return this.mapKeyDataType;
	}
	
	public void setMapKeyDataType(ParameterInfo mapKeyDataType) {
		this.mapKeyDataType = mapKeyDataType;
	}
	
	public ParameterInfo getMapValueDataType() {
		return this.mapValueDataType;
	}
	
	public void setMapValueDataType(ParameterInfo mapValueDataType) {
		this.mapValueDataType = mapValueDataType;
	}
	

}
