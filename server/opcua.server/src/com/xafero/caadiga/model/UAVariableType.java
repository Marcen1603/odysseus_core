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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The UA variable type.
 */
public class UAVariableType extends AbstractNode {

	/** The value rank. */
	@XStreamAsAttribute
	@XStreamAlias("ValueRank")
	private Integer valueRank;

	/** The data type. */
	@XStreamAsAttribute
	@XStreamAlias("DataType")
	private String dataType;

	/** The _abstract. */
	@XStreamAsAttribute
	@XStreamAlias("IsAbstract")
	private Boolean _abstract;

	/** The array dimensions. */
	@XStreamAsAttribute
	@XStreamAlias("ArrayDimensions")
	private String arrayDimensions;

	/**
	 * Gets the value rank.
	 *
	 * @return the value rank
	 */
	public Integer getValueRank() {
		return valueRank;
	}

	/**
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Checks if is abstract.
	 *
	 * @return the boolean
	 */
	public Boolean isAbstract() {
		return _abstract != null && _abstract;
	}

	/**
	 * Gets the array dimensions.
	 *
	 * @return the array dimensions
	 */
	public String getArrayDimensions() {
		return arrayDimensions;
	}
}