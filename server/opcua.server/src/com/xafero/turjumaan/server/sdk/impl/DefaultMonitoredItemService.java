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
import com.inductiveautomation.opcua.stack.core.application.services.MonitoredItemServiceSet;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.SetTriggeringRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.SetTriggeringResponse;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.IMonitoredItem;
import com.xafero.turjumaan.server.sdk.api.ISubscription;
import com.xafero.turjumaan.server.sdk.api.ISubscriptionManager;
import com.xafero.turjumaan.server.sdk.util.LogUtils;

/**
 * The default monitored item service.
 */
public class DefaultMonitoredItemService implements MonitoredItemServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultMonitoredItemService.class);

	/** The manager. */
	private final ISubscriptionManager manager;

	/**
	 * Instantiates a new default monitored item service.
	 *
	 * @param manager
	 *            the manager
	 */
	public DefaultMonitoredItemService(ISubscriptionManager manager) {
		this.manager = manager;
	}

	@Override
	public void onCreateMonitoredItems(ServiceRequest<CreateMonitoredItemsRequest, CreateMonitoredItemsResponse> req)
			throws UaException {
		UInteger subscrId = req.getRequest().getSubscriptionId();
		TimestampsToReturn ttr = req.getRequest().getTimestampsToReturn();
		MonitoredItemCreateRequest[] items = req.getRequest().getItemsToCreate();
		log.info("CreateMonitoredItems --> {} {} {}", subscrId, ttr, LogUtils.toString(items));
		// Get subscription
		ISubscription subscr = manager.getSubscription(subscrId);
		// Build create results
		MonitoredItemCreateResult[] results = new MonitoredItemCreateResult[items.length];
		for (int i = 0; i < items.length; i++) {
			MonitoredItemCreateRequest item = items[i];
			// Create new item
			IMonitoredItem mon = subscr.newMonitoredItem(item.getItemToMonitor().getNodeId(),
					AttributeIds.findById(item.getItemToMonitor().getAttributeId()), item.getMonitoringMode(),
					item.getRequestedParameters().getSamplingInterval(), item.getRequestedParameters().getQueueSize(),
					item.getRequestedParameters().getClientHandle());
			// Get revised parameters
			UInteger monitorId = mon.getMonitoredItemId();
			Double revisedSampling = mon.getRevisedSamplingInterval();
			UInteger revisedQueue = mon.getRevisedQueueSize();
			// Build stack item result
			StatusCode code = StatusCode.GOOD;
			ExtensionObject filtered = null;
			results[i] = new MonitoredItemCreateResult(code, monitorId, revisedSampling, revisedQueue, filtered);
		}
		// Build and send response
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DiagnosticInfo[] diagInfos = null;
		CreateMonitoredItemsResponse cmr = new CreateMonitoredItemsResponse(header, results, diagInfos);
		req.setResponse(cmr);
	}

	@Override
	public void onModifyMonitoredItems(ServiceRequest<ModifyMonitoredItemsRequest, ModifyMonitoredItemsResponse> req)
			throws UaException {
		throw new UnsupportedOperationException("onModifyMonitoredItems");
	}

	@Override
	public void onDeleteMonitoredItems(ServiceRequest<DeleteMonitoredItemsRequest, DeleteMonitoredItemsResponse> req)
			throws UaException {
		throw new UnsupportedOperationException("onDeleteMonitoredItems");
	}

	@Override
	public void onSetMonitoringMode(ServiceRequest<SetMonitoringModeRequest, SetMonitoringModeResponse> req)
			throws UaException {
		throw new UnsupportedOperationException("onSetMonitoringMode");
	}

	@Override
	public void onSetTriggering(ServiceRequest<SetTriggeringRequest, SetTriggeringResponse> req) throws UaException {
		throw new UnsupportedOperationException("onSetTriggering");
	}
}