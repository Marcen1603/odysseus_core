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
 * The UA object.
 */
public class UAObject extends AbstractNode {

	/** The symbolic name. */
	@XStreamAsAttribute
	@XStreamAlias("SymbolicName")
	private String symbolicName;

	/** The event notifier. */
	@XStreamAsAttribute
	@XStreamAlias("EventNotifier")
	private Integer eventNotifier;

	/**
	 * Gets the symbolic name.
	 *
	 * @return the symbolic name
	 */
	public String getSymbolicName() {
		return symbolicName;
	}

	/**
	 * Gets the event notifier.
	 *
	 * @return the event notifier
	 */
	public Integer getEventNotifier() {
		return eventNotifier;
	}
}