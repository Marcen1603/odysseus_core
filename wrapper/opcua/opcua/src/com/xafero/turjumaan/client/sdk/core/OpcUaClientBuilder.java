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
package com.xafero.turjumaan.client.sdk.core;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.inductiveautomation.opcua.stack.client.UaTcpClient;
import com.inductiveautomation.opcua.stack.client.UaTcpClientBuilder;
import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.structured.EndpointDescription;
import com.xafero.turjumaan.core.sdk.api.ICertificateLoader;
import com.xafero.turjumaan.core.sdk.impl.KeyStoreLoader;
import com.xafero.turjumaan.core.sdk.impl.KeyStoreLoaderException;

/**
 * The OPC UA client builder.
 */
public class OpcUaClientBuilder {

	/** The app name. */
	private LocalizedText appName;

	/** The app uri. */
	private String appUri;

	/** The loader. */
	private ICertificateLoader loader;

	/** The use highest level. */
	private boolean useHighestLevel;

	/** The use anonymous token. */
	private boolean useAnonymousToken;

	/**
	 * Instantiates a new OPC UA client builder.
	 */
	public OpcUaClientBuilder() {
		appName = LocalizedText.english("Stack Example Client");
		appUri = String.format("urn:example-client:%s", UUID.randomUUID());
		useHighestLevel = true;
		useAnonymousToken = false;
	}

	/**
	 * Check the loader.
	 */
	private void checkLoader() {
		try {
			if (loader != null)
				return;
			KeyStoreLoader ksLoader = new KeyStoreLoader("client-test-certificate");
			ksLoader.loadFromResource("example-keystore.pfx", "test".toCharArray());
			loader = ksLoader;
		} catch (KeyStoreLoaderException e) {
			throw new RuntimeException("checkLoader", e);
		}
	}

	/**
	 * Builds the OPC UA client.
	 *
	 * @param endpointUrl
	 *            the endpoint URL
	 * @return the OPC UA client
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 * @throws UaException
	 *             the UA exception
	 */
	public OpcUaClient build(String endpointUrl) throws InterruptedException, ExecutionException, UaException {
		checkLoader();
		X509Certificate certificate = loader.getCertificate();
		KeyPair keyPair = loader.getKeyPair();
		UaTcpClientBuilder clientBld = (new UaTcpClientBuilder()).setApplicationName(appName).setApplicationUri(appUri)
				.setCertificate(certificate).setKeyPair(keyPair);
		EndpointDescription endpoint = getBestSecureEndpoint(endpointUrl, useHighestLevel);
		UaTcpClient stackClient = clientBld.build(endpoint);
		return new OpcUaClient(endpoint, stackClient, useAnonymousToken);
	}

	/**
	 * Select highest or worst security level.
	 *
	 * @param endpointUrl
	 *            the endpoint URL
	 * @param highestLevel
	 *            the highest level
	 * @return the best secure endpoint
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
	private EndpointDescription getBestSecureEndpoint(String endpointUrl, boolean highestLevel)
			throws InterruptedException, ExecutionException {
		EndpointDescription[] endpoints = UaTcpClient.getEndpoints(endpointUrl).get();
		Arrays.sort(endpoints, (o1, o2) -> o1.getSecurityLevel().intValue() - o2.getSecurityLevel().intValue());
		return highestLevel ? endpoints[endpoints.length - 1] : endpoints[0];
	}

	/**
	 * Use highest level.
	 *
	 * @param useHighestLevel
	 *            if highest level
	 * @return the OPC UA client builder
	 */
	public OpcUaClientBuilder useHighestLevel(boolean useHighestLevel) {
		this.useHighestLevel = useHighestLevel;
		return this;
	}

	/**
	 * Use anonymous token.
	 *
	 * @param useAnonymousToken
	 *            if anonymous token
	 * @return the OPC UA client builder
	 */
	public OpcUaClientBuilder useAnonymousToken(boolean useAnonymousToken) {
		this.useAnonymousToken = useAnonymousToken;
		return this;
	}
}