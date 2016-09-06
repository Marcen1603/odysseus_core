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
package com.xafero.turjumaan.core.sdk.api;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * The interface for loading certificates.
 */
public interface ICertificateLoader {

	/**
	 * Gets the certificate.
	 *
	 * @return the certificate
	 */
	X509Certificate getCertificate();

	/**
	 * Gets the public and private key pair.
	 *
	 * @return the key pair
	 */
	KeyPair getKeyPair();

}