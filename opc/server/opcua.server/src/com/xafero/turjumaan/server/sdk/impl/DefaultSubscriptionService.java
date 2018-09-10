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

import java.util.Arrays;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.StatusCodes;
import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.application.services.SubscriptionServiceSet;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.NotificationMessage;
import com.inductiveautomation.opcua.stack.core.types.structured.PublishRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.PublishResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.RepublishRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.RepublishResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.SetPublishingModeRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.SetPublishingModeResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import com.inductiveautomation.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import com.xafero.turjumaan.server.sdk.api.ISubscription;
import com.xafero.turjumaan.server.sdk.api.ISubscriptionManager;
import com.xafero.turjumaan.server.sdk.util.LogUtils;

/**
 * The default subscription service.
 */
public class DefaultSubscriptionService implements SubscriptionServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriptionService.class);

	/** The manager. */
	private final ISubscriptionManager manager;

	/**
	 * Instantiates a new default subscription service.
	 *
	 * @param manager
	 *            the manager
	 */
	public DefaultSubscriptionService(ISubscriptionManager manager) {
		this.manager = manager;
	}

	@Override
	public void onCreateSubscription(ServiceRequest<CreateSubscriptionRequest, CreateSubscriptionResponse> req)
			throws UaException {
		Double interval = req.getRequest().getRequestedPublishingInterval();
		Boolean pubEnabled = req.getRequest().getPublishingEnabled();
		log.info("CreateSubscription --> {} {}", interval, pubEnabled);
		// Create subscription
		ISubscription subscr = manager.newSubscription(interval, req.getRequest().getRequestedLifetimeCount(),
				req.getRequest().getRequestedMaxKeepAliveCount());
		// Get revised parameters
		UInteger subscrId = subscr.getSubscriptionId();
		Double revisedInterval = subscr.getRevisedPubInterval();
		UInteger revisedLifetime = subscr.getRevisedLifetime();
		UInteger revisedMaxKeepAlive = subscr.getRevisedMaxKeepAlive();
		// Build response
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		CreateSubscriptionResponse csr = new CreateSubscriptionResponse(header, subscrId, revisedInterval,
				revisedLifetime, revisedMaxKeepAlive);
		req.setResponse(csr);
	}

	@Override
	public void onDeleteSubscriptions(ServiceRequest<DeleteSubscriptionsRequest, DeleteSubscriptionsResponse> req)
			throws UaException {
		UInteger[] subscrIds = req.getRequest().getSubscriptionIds();
		log.info("DeleteSubscriptions --> {}", LogUtils.toString(subscrIds));
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		StatusCode[] results = new StatusCode[subscrIds.length];
		for (int i = 0; i < results.length; i++)
			results[i] = manager.cancelSubscription(subscrIds[i]) ? StatusCode.GOOD
					: new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
		DiagnosticInfo[] diagnosticInfos = null;
		DeleteSubscriptionsResponse dsr = new DeleteSubscriptionsResponse(header, results, diagnosticInfos);
		req.setResponse(dsr);
	}

	@Override
	public void onModifySubscription(ServiceRequest<ModifySubscriptionRequest, ModifySubscriptionResponse> req)
			throws UaException {
		UInteger subscrId = req.getRequest().getSubscriptionId();
		log.info("ModifySubscription --> {}", subscrId);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		// Get subscription
		ISubscription subscr = manager.getSubscription(subscrId);
		subscr.modify(req.getRequest().getRequestedPublishingInterval(), req.getRequest().getRequestedLifetimeCount(),
				req.getRequest().getRequestedMaxKeepAliveCount());
		// Get revised parameters
		Double revisedInterval = subscr.getRevisedPubInterval();
		UInteger revisedLifetime = subscr.getRevisedLifetime();
		UInteger revisedMaxKeepAlive = subscr.getRevisedMaxKeepAlive();
		// Build response
		ModifySubscriptionResponse msr = new ModifySubscriptionResponse(header, revisedInterval, revisedLifetime,
				revisedMaxKeepAlive);
		req.setResponse(msr);
	}

	@Override
	public void onPublish(ServiceRequest<PublishRequest, PublishResponse> req) throws UaException {
		SubscriptionAcknowledgement[] acks = req.getRequest().getSubscriptionAcknowledgements();
		log.debug("Publish --> {}", LogUtils.toString(acks));
		// Build status codes for acknowledgments
		StatusCode[] results = new StatusCode[acks.length];
		for (int i = 0; i < results.length; i++)
			results[i] = StatusCode.GOOD;
		// Access subscription
		Iterator<ISubscription> it = manager.getAllSubscriptions().iterator();
		if (!it.hasNext())
			return;
		ISubscription subscr = it.next();
		// Get current notification
		UInteger[] availableSeqNums = subscr.getAvailableSeqNums();
		NotificationMessage notifMsg = subscr.nextNotificationMessage();
		// Should the client ask again?
		Boolean moreNotifs = subscr.hasNotificationMessage();
		// Build and send response
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		UInteger subscrId = subscr.getSubscriptionId();
		DiagnosticInfo[] diagnosticInfos = null;
		PublishResponse pr = new PublishResponse(header, subscrId, availableSeqNums, moreNotifs, notifMsg, results,
				diagnosticInfos);
		req.setResponse(pr);
	}

	@Override
	public void onRepublish(ServiceRequest<RepublishRequest, RepublishResponse> req) throws UaException {
		throw new UnsupportedOperationException("onRepublish");
	}

	@Override
	public void onSetPublishingMode(ServiceRequest<SetPublishingModeRequest, SetPublishingModeResponse> req)
			throws UaException {
		Boolean pubEnabled = req.getRequest().getPublishingEnabled();
		UInteger[] subscrIds = req.getRequest().getSubscriptionIds();
		log.info("SetPublishingMode --> {} {}", pubEnabled, Arrays.toString(subscrIds));
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DiagnosticInfo[] diagInfos = null;
		StatusCode[] results = new StatusCode[subscrIds.length];
		for (int i = 0; i < results.length; i++)
			results[i] = StatusCode.GOOD;
		SetPublishingModeResponse spmr = new SetPublishingModeResponse(header, results, diagInfos);
		req.setResponse(spmr);
	}

	@Override
	public void onTransferSubscriptions(ServiceRequest<TransferSubscriptionsRequest, TransferSubscriptionsResponse> req)
			throws UaException {
		throw new UnsupportedOperationException("onTransferSubscriptions");
	}
}