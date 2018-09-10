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
 * The UA variable.
 */
public class UAVariable extends AbstractNode {

	/** The value. */
	@XStreamAlias("Value")
	private Value value;

	/** The data type. */
	@XStreamAsAttribute
	@XStreamAlias("DataType")
	private String dataType;

	/** The value rank. */
	@XStreamAsAttribute
	@XStreamAlias("ValueRank")
	private Integer valueRank;

	/** The min sampling interval. */
	@XStreamAsAttribute
	@XStreamAlias("MinimumSamplingInterval")
	private Long minSamplingInterval;

	/** The access level. */
	@XStreamAsAttribute
	@XStreamAlias("AccessLevel")
	private Integer accessLevel;

	/** The user access level. */
	@XStreamAsAttribute
	@XStreamAlias("UserAccessLevel")
	private Integer userAccessLevel;

	/** The symbolic name. */
	@XStreamAsAttribute
	@XStreamAlias("SymbolicName")
	private String symbolicName;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Value getValue() {
		return value;
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
	 * Gets the value rank.
	 *
	 * @return the value rank
	 */
	public Integer getValueRank() {
		return valueRank;
	}

	/**
	 * Gets the min sampling interval.
	 *
	 * @return the min sampling interval
	 */
	public Long getMinSamplingInterval() {
		return minSamplingInterval;
	}

	/**
	 * Gets the access level.
	 *
	 * @return the access level
	 */
	public Integer getAccessLevel() {
		return accessLevel;
	}

	/**
	 * Gets the user access level.
	 *
	 * @return the user access level
	 */
	public Integer getUserAccessLevel() {
		return userAccessLevel;
	}

	/**
	 * Gets the symbolic name.
	 *
	 * @return the symbolic name
	 */
	public String getSymbolicName() {
		return symbolicName;
	}
}