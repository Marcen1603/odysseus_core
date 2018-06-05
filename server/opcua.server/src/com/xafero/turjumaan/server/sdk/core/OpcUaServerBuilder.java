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
package com.xafero.turjumaan.server.sdk.core;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.UUID;

import com.inductiveautomation.opcua.stack.core.application.CertificateManager;
import com.inductiveautomation.opcua.stack.core.application.DirectoryCertificateManager;
import com.inductiveautomation.opcua.stack.core.security.SecurityPolicy;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MessageSecurityMode;
import com.inductiveautomation.opcua.stack.server.tcp.UaTcpServer;
import com.inductiveautomation.opcua.stack.server.tcp.UaTcpServerBuilder;
import com.xafero.turjumaan.core.sdk.api.ICertificateLoader;
import com.xafero.turjumaan.core.sdk.impl.KeyStoreLoader;
import com.xafero.turjumaan.core.sdk.impl.KeyStoreLoaderException;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

/**
 * The OPC UA server builder.
 */
public class OpcUaServerBuilder {

	/** The server name. */
	private String serverName;

	/** The application name. */
	private LocalizedText appName;

	/** The application URI. */
	private String appUri;

	/** The certification manager. */
	private CertificateManager certMgr;

	/** The loader. */
	private ICertificateLoader loader;

	/** The patch empty endpoints. */
	private boolean patchEmptyEndpoints;

	/** The policy. */
	private SecurityPolicy policy;

	/** The mode. */
	private MessageSecurityMode mode;

	/** The bind address. */
	private String bindAddress;

	/**
	 * Instantiates a new OPC UA server builder.
	 */
	public OpcUaServerBuilder() {
		serverName = "example";
		appName = LocalizedText.english("Stack Example Server");
		appUri = String.format("urn:example-server:%s", UUID.randomUUID());
		policy = SecurityPolicy.None;
		mode = MessageSecurityMode.None;
		bindAddress = null;
	}

	/**
	 * Check the loader.
	 */
	private void checkLoader() {
		try {
			if (loader != null)
				return;
			KeyStoreLoader ksLoader = new KeyStoreLoader("server-test-certificate");
			ksLoader.loadFromResource("example-keystore.pfx", "test".toCharArray());
			loader = ksLoader;
		} catch (KeyStoreLoaderException e) {
			throw new RuntimeException("checkLoader", e);
		}
	}

	/**
	 * Check the manager.
	 *
	 * @param certificate
	 *            the certificate
	 * @param keyPair
	 *            the key pair
	 */
	private void checkManager(X509Certificate certificate, KeyPair keyPair) {
		if (certMgr != null)
			return;
		File dir = new File("./security/");
		if (!dir.exists() && !dir.mkdirs())
			throw new UnsupportedOperationException("Unable to create security directory!");
		DirectoryCertificateManager certificateManager = new DirectoryCertificateManager(keyPair, certificate, dir);
		certMgr = certificateManager;
	}

	/**
	 * Builds the OPC UA server instance.
	 *
	 * @param endpointUrl
	 *            the endpoint URL
	 * @param model
	 *            the model
	 * @return the OPC UA server
	 */
	public OpcUaServer build(String endpointUrl, INodeModel model) {
		checkLoader();
		X509Certificate certificate = loader.getCertificate();
		KeyPair keyPair = loader.getKeyPair();
		checkManager(certificate, keyPair);
		UaTcpServerBuilder serverBld = (new UaTcpServerBuilder()).setServerName(serverName).setApplicationName(appName)
				.setApplicationUri(appUri).setCertificateManager(certMgr);
		UaTcpServer stackServer = serverBld.build();
		return new OpcUaServer(endpointUrl, stackServer, certificate, model, patchEmptyEndpoints, policy, mode,
				bindAddress);
	}

	/**
	 * Patch empty endpoints.
	 *
	 * @param patchEmptyEndpoints
	 *            the patch empty endpoints
	 * @return the OPC UA server builder
	 */
	public OpcUaServerBuilder patchEmptyEndpoints(boolean patchEmptyEndpoints) {
		this.patchEmptyEndpoints = patchEmptyEndpoints;
		return this;
	}

	/**
	 * Sets the policy.
	 *
	 * @param policy
	 *            the security policy
	 * @return the OPC UA server builder
	 */
	public OpcUaServerBuilder policy(SecurityPolicy policy) {
		this.policy = policy;
		return this;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode
	 *            the security mode
	 * @return the OPC UA server builder
	 */
	public OpcUaServerBuilder mode(MessageSecurityMode mode) {
		this.mode = mode;
		return this;
	}

	/**
	 * Sets the bind address.
	 *
	 * @param bindAddress
	 *            the bind address
	 * @return the OPC UA server builder
	 */
	public OpcUaServerBuilder bindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
		return this;
	}
}