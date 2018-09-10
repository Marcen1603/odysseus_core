/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.caadiga.model;

import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * The UA NodeSet.
 */
@XStreamAlias("UANodeSet")
public class UANodeSet {

	/** The xmlns. */
	@XStreamAsAttribute
	final String xmlns = "http://opcfoundation.org/UA/2011/03/UANodeSet.xsd";

	/** The xsd. */
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	final String xsd = "http://www.w3.org/2001/XMLSchema";

	/** The xsi. */
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	final String xsi = xsd + "-instance";

	/** The version. */
	@XStreamAsAttribute
	@XStreamAlias("Version")
	private String version;

	/**
	 * Should be of type 'Date', but that doesn't work due to the time accuracy
	 * not available in Java!.
	 */
	@XStreamAsAttribute
	@XStreamAlias("LastModified")
	private/* Date */String lastModified;

	/** The aliases. */
	@XStreamAlias("Aliases")
	private List<Alias> aliases;

	/** The objects. */
	@XStreamImplicit(itemFieldName = "UAObject")
	private List<UAObject> objects;

	/** The data types. */
	@XStreamImplicit(itemFieldName = "UADataType")
	private List<UADataType> dataTypes;

	/** The reference types. */
	@XStreamImplicit(itemFieldName = "UAReferenceType")
	private List<UAReferenceType> referenceTypes;

	/** The object types. */
	@XStreamImplicit(itemFieldName = "UAObjectType")
	private List<UAObjectType> objectTypes;

	/** The variable types. */
	@XStreamImplicit(itemFieldName = "UAVariableType")
	private List<UAVariableType> variableTypes;

	/** The variables. */
	@XStreamImplicit(itemFieldName = "UAVariable")
	private List<UAVariable> variables;

	/** The methods. */
	@XStreamImplicit(itemFieldName = "UAMethod")
	private List<UAMethod> methods;

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the last modified.
	 *
	 * @return the last modified
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * Gets the aliases.
	 *
	 * @return the aliases
	 */
	public List<Alias> getAliases() {
		return aliases;
	}

	/**
	 * Gets the objects.
	 *
	 * @return the objects
	 */
	public List<UAObject> getObjects() {
		return objects;
	}

	/**
	 * Gets the data types.
	 *
	 * @return the data types
	 */
	public List<UADataType> getDataTypes() {
		if (dataTypes == null)
			return Collections.emptyList();
		return dataTypes;
	}

	/**
	 * Gets the reference types.
	 *
	 * @return the reference types
	 */
	public List<UAReferenceType> getReferenceTypes() {
		if (referenceTypes == null)
			return Collections.emptyList();
		return referenceTypes;
	}

	/**
	 * Gets the object types.
	 *
	 * @return the object types
	 */
	public List<UAObjectType> getObjectTypes() {
		if (objectTypes == null)
			return Collections.emptyList();
		return objectTypes;
	}

	/**
	 * Gets the variable types.
	 *
	 * @return the variable types
	 */
	public List<UAVariableType> getVariableTypes() {
		if (variableTypes == null)
			return Collections.emptyList();
		return variableTypes;
	}

	/**
	 * Gets the variables.
	 *
	 * @return the variables
	 */
	public List<UAVariable> getVariables() {
		return variables;
	}

	/**
	 * Gets the methods.
	 *
	 * @return the methods
	 */
	public List<UAMethod> getMethods() {
		if (methods == null)
			return Collections.emptyList();
		return methods;
	}
}