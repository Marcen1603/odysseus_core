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
package de.uniol.inf.is.odysseus.server.opcua.binding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;

/**
 * The binding for the security service.
 */
public class SecurityServiceBinding {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(SecurityServiceBinding.class);

	/** The provider. */
	private static ISecurityProvider provider;

	/**
	 * Bind security.
	 *
	 * @param prov
	 *            the provider
	 */
	public void bindSecurity(ISecurityProvider prov) {
		log.info("Got security ({})...", prov.getClass().getSimpleName());
		provider = prov;
	}

	/**
	 * Unbind security.
	 *
	 * @param prov
	 *            the provider
	 */
	public void unbindSecurity(ISecurityProvider prov) {
		log.info("Lost security ({})...", prov.getClass().getSimpleName());
		provider = null;
	}

	/**
	 * Gets the security.
	 *
	 * @return the security
	 */
	public static ISecurityProvider getSecurity() {
		return provider;
	}
}