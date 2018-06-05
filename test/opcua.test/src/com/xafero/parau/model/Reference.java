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
package com.xafero.parau.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A reference.
 */
public class Reference {

	/** The bind. */
	@XStreamAsAttribute
	private String bind;

	/** The cardinality. */
	@XStreamAsAttribute
	private String cardinality;

	/** The interface name. */
	@XStreamAsAttribute
	@XStreamAlias("interface")
	private String interfaceName;

	/** The name. */
	@XStreamAsAttribute
	private String name;

	/** The policy. */
	@XStreamAsAttribute
	private String policy;

	/** The unbind. */
	@XStreamAsAttribute
	private String unbind;

	/**
	 * Gets the bind.
	 *
	 * @return the string
	 */
	public String bind() {
		return bind;
	}

	/**
	 * Gets the cardinality.
	 *
	 * @return the string
	 */
	public String cardinality() {
		return cardinality;
	}

	/**
	 * Gets the interface name.
	 *
	 * @return the string
	 */
	public String interfaceName() {
		return interfaceName;
	}

	/**
	 * Gets the name.
	 *
	 * @return the string
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets the policy.
	 *
	 * @return the string
	 */
	public String policy() {
		return policy;
	}

	/**
	 * Gets the unbind.
	 *
	 * @return the string
	 */
	public String unbind() {
		return unbind;
	}
}