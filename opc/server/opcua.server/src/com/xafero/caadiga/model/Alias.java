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
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * The alias.
 */
@XStreamAlias("Alias")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "nodeId" })
public class Alias {

	/** The alias. */
	@XStreamAsAttribute
	@XStreamAlias("Alias")
	private String alias;

	/** The node id. */
	private String nodeId;

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public String getNodeId() {
		return nodeId;
	}
}