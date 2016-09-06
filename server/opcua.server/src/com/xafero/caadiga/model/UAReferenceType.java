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
 * The UA reference type.
 */
public class UAReferenceType extends AbstractNode {

	/** The inverse name. */
	@XStreamAlias("InverseName")
	private String inverseName;

	/** The _abstract. */
	@XStreamAsAttribute
	@XStreamAlias("IsAbstract")
	private Boolean _abstract;

	/** The symmetric. */
	@XStreamAsAttribute
	@XStreamAlias("Symmetric")
	private Boolean symmetric;

	/**
	 * Gets the inverse name.
	 *
	 * @return the inverse name
	 */
	public String getInverseName() {
		return inverseName;
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
	 * Checks if is symmetric.
	 *
	 * @return the boolean
	 */
	public Boolean isSymmetric() {
		return symmetric != null && symmetric;
	}
}