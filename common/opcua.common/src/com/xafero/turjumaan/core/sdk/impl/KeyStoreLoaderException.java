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
package com.xafero.turjumaan.core.sdk.impl;

/**
 * The exception for errors of a key store loader.
 */
public class KeyStoreLoaderException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5691530575387334152L;

	/** The alias. */
	private final String alias;

	/**
	 * Instantiates a new key store loader exception.
	 *
	 * @param alias
	 *            the alias
	 * @param e
	 *            the exception
	 */
	public KeyStoreLoaderException(String alias, Exception e) {
		super(String.format("Couldn't load the alias '%s'!", alias), e);
		this.alias = alias;
	}

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}
}