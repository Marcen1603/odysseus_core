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
 * A provide.
 */
public class Provide {

	/** The interface name. */
	@XStreamAsAttribute
	@XStreamAlias("interface")
	private String interfaceName;

	/**
	 * Sets the interface name.
	 *
	 * @param interfaceName
	 *            the interface name
	 * @return the provide
	 */
	public Provide interfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	/**
	 * Gets the interface name.
	 *
	 * @return the string
	 */
	public String interfaceName() {
		return interfaceName;
	}
}