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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.MethodServiceSet;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.structured.CallMethodRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CallMethodResult;
import com.inductiveautomation.opcua.stack.core.types.structured.CallRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CallResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.xafero.turjumaan.server.sdk.api.INodeExecutor;
import com.xafero.turjumaan.server.sdk.util.LogUtils;

/**
 * The default method service.
 */
public class DefaultMethodService implements MethodServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultMethodService.class);

	/** The executor. */
	private final INodeExecutor executor;

	/**
	 * Instantiates a new default method service.
	 *
	 * @param executor
	 *            the executor
	 */
	public DefaultMethodService(INodeExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void onCall(ServiceRequest<CallRequest, CallResponse> req) throws UaException {
		CallMethodRequest[] cmr = req.getRequest().getMethodsToCall();
		log.info("Call --> {}", LogUtils.toString(cmr));
		CallMethodResult[] results = new CallMethodResult[cmr.length];
		int idx = 0;
		for (CallMethodRequest cm : cmr) {
			// Get parameters
			NodeId methId = cm.getMethodId();
			NodeId instId = cm.getObjectId();
			Variant[] args = cm.getInputArguments();
			// Prepare result container
			List<Object> out = new LinkedList<Object>();
			StatusCode code = executor.execute(instId, methId, args, out);
			// Set other unimportant stuff
			StatusCode[] inputCodes = new StatusCode[args.length];
			for (int i = 0; i < inputCodes.length; i++)
				inputCodes[i] = StatusCode.GOOD;
			Variant[] outputArgs = out.stream().map(v -> new Variant(v)).toArray(Variant[]::new);
			DiagnosticInfo[] inputDiags = null;
			// One method finished
			results[idx++] = new CallMethodResult(code, inputCodes, inputDiags, outputArgs);
		}
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DiagnosticInfo[] diagInfos = null;
		CallResponse br = new CallResponse(header, results, diagInfos);
		req.setResponse(br);
	}
}