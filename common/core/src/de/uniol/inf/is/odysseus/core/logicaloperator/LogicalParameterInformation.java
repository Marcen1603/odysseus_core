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

import java.util.List;

/**
 * @author DGeesen
 * 
 */
public class LogicalParameterInformation {

	private Class<?> parameterClass;
	private String name;
	private boolean list;
	private String doc;
	private boolean mandatory;
	private List<String> possibleValues;
	private boolean possibleValuesAreDynamic;
	private boolean deprecated;

	public Class<?> getParameterClass() {
		return parameterClass;
	}

	public void setParameterClass(Class<?> parameterClass) {
		this.parameterClass = parameterClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
		
	}

	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public void setPossibleValuesAreDynamic(boolean possibleValuesAreDynamic) {
		this.possibleValuesAreDynamic = possibleValuesAreDynamic;
	}

	public boolean arePossibleValuesDynamic() {
		return possibleValuesAreDynamic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " (" + parameterClass + ") list=" + list;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

}
