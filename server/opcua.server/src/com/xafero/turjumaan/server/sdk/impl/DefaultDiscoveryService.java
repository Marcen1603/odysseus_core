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
package com.xafero.turjumaan.server.sdk.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.DiscoveryServiceSet;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.structured.ApplicationDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.EndpointDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.FindServersRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.FindServersResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.GetEndpointsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.GetEndpointsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.RegisterServerRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.RegisterServerResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;

/**
 * The default discovery service.
 */
public class DefaultDiscoveryService implements DiscoveryServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultDiscoveryService.class);

	@Override
	public void onFindServers(ServiceRequest<FindServersRequest, FindServersResponse> req) throws UaException {
		String url = req.getRequest().getEndpointUrl();
		log.info("FindServers --> {}", url);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		ApplicationDescription[] servers = new ApplicationDescription[] { req.getServer().getApplicationDescription() };
		FindServersResponse fsr = new FindServersResponse(header, servers);
		req.setResponse(fsr);
	}

	@Override
	public void onGetEndpoints(ServiceRequest<GetEndpointsRequest, GetEndpointsResponse> req) throws UaException {
		String url = req.getRequest().getEndpointUrl();
		log.info("GetEndpoints --> {}", url);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		EndpointDescription[] endpoints = req.getServer().getEndpointDescriptions();
		GetEndpointsResponse ger = new GetEndpointsResponse(header, endpoints);
		req.setResponse(ger);
	}

	@Override
	public void onRegisterServer(ServiceRequest<RegisterServerRequest, RegisterServerResponse> req) throws UaException {
		throw new UnsupportedOperationException("onRegisterServer");
	}
}