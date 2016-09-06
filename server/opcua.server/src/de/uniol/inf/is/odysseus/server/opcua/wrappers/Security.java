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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;
import de.uniol.inf.is.odysseus.server.opcua.binding.SecurityServiceBinding;

/**
 * The security provider of Odysseus.
 */
@Description("The security of Odysseus")
public class Security {

	/**
	 * Gets the default certificate.
	 *
	 * @return the default certificate
	 * @throws GeneralSecurityException
	 *             the general security exception
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("Its default certificate")
	public Certificate getDefaultCertificate() throws GeneralSecurityException {
		return getSecurity().getDefaultCertificate();
	}

	/**
	 * Gets the default key.
	 *
	 * @return the default key
	 * @throws GeneralSecurityException
	 *             the general security exception
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("Its default key")
	public PrivateKey getDefaultKey() throws GeneralSecurityException {
		return getSecurity().getDefaultKey();
	}

	/**
	 * Gets the key managers.
	 *
	 * @return the key managers
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All key managers")
	public KeyManager[] getKeyManagers() {
		return getSecurity().getKeyManagers();
	}

	/**
	 * Gets the trust managers.
	 *
	 * @return the trust managers
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All trust managers")
	public TrustManager[] getTrustManagers() {
		return getSecurity().getTrustManagers();
	}

	/**
	 * Gets the security.
	 *
	 * @return the security
	 */
	private ISecurityProvider getSecurity() {
		return SecurityServiceBinding.getSecurity();
	}
}