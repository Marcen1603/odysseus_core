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
 * A component.
 */
@XStreamAlias("scr:component")
public class Component {

	/** The scr. */
	@XStreamAlias("xmlns:scr")
	@XStreamAsAttribute
	private final String scr = "http://www.osgi.org/xmlns/scr/v1.1.0";

	/** The name. */
	@XStreamAsAttribute
	private String name;

	/** The implementation. */
	private Implementation implementation;

	/** The service. */
	private Service service;

	/** The reference. */
	private Reference reference;

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name
	 * @return the component
	 */
	public Component name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Sets the implementation.
	 *
	 * @param implementation
	 *            the implementation
	 * @return the component
	 */
	public Component impl(Implementation implementation) {
		this.implementation = implementation;
		return this;
	}

	/**
	 * Sets the service.
	 *
	 * @param service
	 *            the service
	 * @return the component
	 */
	public Component svc(Service service) {
		this.service = service;
		return this;
	}

	/**
	 * Sets the reference.
	 *
	 * @param reference
	 *            the reference
	 * @return the component
	 */
	public Component ref(Reference reference) {
		this.reference = reference;
		return this;
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
	 * Gets the implementation.
	 *
	 * @return the implementation
	 */
	public Implementation impl() {
		return implementation;
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public Service svc() {
		return service;
	}

	/**
	 * Gets the reference.
	 *
	 * @return the reference
	 */
	public Reference ref() {
		return reference;
	}
}