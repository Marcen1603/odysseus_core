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

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.security.SecurityPolicy;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MessageSecurityMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.UserTokenType;
import com.inductiveautomation.opcua.stack.core.types.structured.UserTokenPolicy;
import com.inductiveautomation.opcua.stack.server.tcp.UaTcpServer;
import com.xafero.turjumaan.core.sdk.util.BasicUtils;
import com.xafero.turjumaan.server.sdk.api.INodeModel;
import com.xafero.turjumaan.server.sdk.api.ISubscriptionManager;
import com.xafero.turjumaan.server.sdk.flow.SubscriptionRepository;
import com.xafero.turjumaan.server.sdk.impl.DefaultAttributeService;
import com.xafero.turjumaan.server.sdk.impl.DefaultDiscoveryService;
import com.xafero.turjumaan.server.sdk.impl.DefaultMethodService;
import com.xafero.turjumaan.server.sdk.impl.DefaultMonitoredItemService;
import com.xafero.turjumaan.server.sdk.impl.DefaultQueryService;
import com.xafero.turjumaan.server.sdk.impl.DefaultSessionService;
import com.xafero.turjumaan.server.sdk.impl.DefaultSubscriptionService;
import com.xafero.turjumaan.server.sdk.impl.DefaultViewService;

/**
 * The OPC UA server.
 */
public class OpcUaServer implements Runnable, Closeable {

	/** The Constant log. */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(OpcUaServer.class);

	/** The stack server. */
	private final UaTcpServer stackServer;

	/** The subscription manager. */
	private final ISubscriptionManager subscrMgr;

	/** The model. */
	private final INodeModel model;

	/** The patch empty endpoints. */
	private final boolean patchEmptyEndpoints;

	/**
	 * Instantiates a new OPC UA server.
	 *
	 * @param endpointUrl
	 *            the endpoint URL
	 * @param stackServer
	 *            the stack server
	 * @param certificate
	 *            the certificate
	 * @param model
	 *            the model
	 * @param patchEmptyEndpoints
	 *            the patch empty endpoints
	 * @param policy
	 *            the policy
	 * @param mode
	 *            the mode
	 * @param bindAddress
	 *            the bind address
	 */
	OpcUaServer(String endpointUrl, UaTcpServer stackServer, X509Certificate certificate, INodeModel model,
			boolean patchEmptyEndpoints, SecurityPolicy policy, MessageSecurityMode mode, String bindAddress) {
		this.stackServer = stackServer;
		this.model = model;
		this.patchEmptyEndpoints = patchEmptyEndpoints;
		createEndpoint(endpointUrl, certificate, bindAddress, policy, mode);
		addDefaultUsers(policy);
		subscrMgr = new SubscriptionRepository(model);
		addDefaultServices(certificate, model);
	}

	/**
	 * Adds the default services.
	 *
	 * @param certificate
	 *            the certificate
	 * @param model
	 *            the model
	 */
	private void addDefaultServices(X509Certificate certificate, INodeModel model) {
		stackServer.addServiceSet(new DefaultDiscoveryService());
		stackServer.addServiceSet(new DefaultSessionService(certificate));
		stackServer.addServiceSet(new DefaultAttributeService(model, model));
		stackServer.addServiceSet(new DefaultQueryService());
		stackServer.addServiceSet(new DefaultViewService(model, model));
		stackServer.addServiceSet(new DefaultSubscriptionService(subscrMgr));
		stackServer.addServiceSet(new DefaultMonitoredItemService(subscrMgr));
		stackServer.addServiceSet(new DefaultMethodService(model));
	}

	/**
	 * Adds the default users.
	 *
	 * @param policy
	 *            the policy
	 */
	private void addDefaultUsers(SecurityPolicy policy) {
		List<UserTokenPolicy> policies = stackServer.getUserTokenPolicies();
		policies.add(createAnonymousUser(policy));
	}

	/**
	 * Creates the anonymous user.
	 *
	 * @param policy
	 *            the policy
	 * @return the user token policy
	 */
	private UserTokenPolicy createAnonymousUser(SecurityPolicy policy) {
		String policyId = "AnonymousIdentityToken";
		UserTokenType tokenType = UserTokenType.Anonymous;
		String issuedTokenType = null;
		String issuerEndpointUrl = null;
		String securityPolicyUri = "http://opcfoundation.org/UA/SecurityPolicy#" + policy.name();
		return new UserTokenPolicy(policyId, tokenType, issuedTokenType, issuerEndpointUrl, securityPolicyUri);
	}

	/**
	 * Creates the endpoint.
	 *
	 * @param endpointUri
	 *            the endpoint URI
	 * @param certificate
	 *            the certificate
	 * @param bindAddress
	 *            the bind address
	 * @param policy
	 *            the policy
	 * @param mode
	 *            the mode
	 */
	private void createEndpoint(String endpointUri, X509Certificate certificate, String bindAddress,
			SecurityPolicy policy, MessageSecurityMode mode) {
		stackServer.addEndpoint(endpointUri, bindAddress, certificate, policy, mode);
	}

	@Override
	public void run() {
		stackServer.startup();
		if (patchEmptyEndpoints)
			Patches.patchCrazyEndpoint();
	}

	@Override
	public void close() throws IOException {
		if (model instanceof Closeable)
			((Closeable) model).close();
		subscrMgr.close();
		stackServer.shutdown();
	}

	/**
	 * Gets the endpoint URIs.
	 *
	 * @return the endpoint URIs
	 */
	public URI[] getEndpointURIs() {
		return stackServer.getEndpointUrls().stream().map(e -> BasicUtils.toURI(e)).toArray(size -> new URI[size]);
	}

	/**
	 * Gets the security modes.
	 *
	 * @return the security modes
	 */
	public MessageSecurityMode[] getSecurityModes() {
		return stackServer.getEndpoints().stream().map(e -> e.getMessageSecurity())
				.toArray(size -> new MessageSecurityMode[size]);
	}
}