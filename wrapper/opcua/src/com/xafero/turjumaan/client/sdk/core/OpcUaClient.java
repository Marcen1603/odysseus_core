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

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.client.UaTcpClient;
import com.inductiveautomation.opcua.stack.core.serialization.UaResponseMessage;
import com.inductiveautomation.opcua.stack.core.serialization.UaSerializable;
import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MessageSecurityMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MonitoringMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.inductiveautomation.opcua.stack.core.types.structured.ActivateSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ActivateSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.AnonymousIdentityToken;
import com.inductiveautomation.opcua.stack.core.types.structured.ApplicationDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CallMethodRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CallMethodResult;
import com.inductiveautomation.opcua.stack.core.types.structured.CallRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CallResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CloseSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CloseSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.DataChangeNotification;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.EndpointDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemNotification;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoringParameters;
import com.inductiveautomation.opcua.stack.core.types.structured.NotificationMessage;
import com.inductiveautomation.opcua.stack.core.types.structured.PublishRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.PublishResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadValueId;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.RequestHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.SignatureData;
import com.inductiveautomation.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import com.inductiveautomation.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import com.inductiveautomation.opcua.stack.core.types.structured.ViewDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteValue;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.util.StatusUtils;

/**
 * The OPC UA client.
 */
public class OpcUaClient implements Closeable {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(OpcUaClient.class);

	/** The endpoint. */
	private final EndpointDescription endpoint;

	/** The stack client. */
	private final UaTcpClient stackClient;

	/** The use anonymous token. */
	private final boolean useAnonymousToken;

	/** The request handle. */
	private final AtomicLong requestHandle;

	/** The session name. */
	private final String sessionName;

	/** The client handle. */
	private final AtomicLong clientHandle;

	/** The client handle nodes. */
	private final Map<UInteger, NodeId> clientHandleNodes;

	/** The auth token. */
	private NodeId authToken;

	/** The session id. */
	private NodeId sessionId;

	/**
	 * Instantiates a new OPC UA client.
	 *
	 * @param endpoint
	 *            the endpoint
	 * @param stackClient
	 *            the stack client
	 * @param useAnonymousToken
	 *            the use anonymous token
	 */
	OpcUaClient(EndpointDescription endpoint, UaTcpClient stackClient, boolean useAnonymousToken) {
		this.endpoint = endpoint;
		this.stackClient = stackClient;
		this.useAnonymousToken = useAnonymousToken;
		this.authToken = NodeId.NULL_VALUE;
		this.sessionId = NodeId.NULL_VALUE;
		this.requestHandle = new AtomicLong(1L);
		this.sessionName = getHostnameSession();
		this.clientHandle = new AtomicLong(1L);
		this.clientHandleNodes = new HashMap<>();
	}

	/**
	 * Gets the host name and session.
	 *
	 * @return the host name and session
	 */
	private String getHostnameSession() {
		final String sdkPrefix = "turjumaan";
		try {
			return sdkPrefix + "@" + InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return sdkPrefix;
		}
	}

	/**
	 * Gets the request header.
	 *
	 * @return the request header
	 */
	private RequestHeader getRequestHeader() {
		DateTime timestamp = DateTime.now();
		UInteger handle = uint(requestHandle.getAndIncrement());
		UInteger returnDiagnostics = uint(0);
		String auditEntryId = null;
		UInteger timeoutHint = uint(10000);
		ExtensionObject additional = new ExtensionObject((UaSerializable) null, NodeId.NULL_VALUE);
		return new RequestHeader(authToken, timestamp, handle, returnDiagnostics, auditEntryId, timeoutHint,
				additional);
	}

	/**
	 * Creates the session.
	 *
	 * @return the completable future
	 */
	private CompletableFuture<UaResponseMessage> createSession() {
		log.info("Creating a session...");
		RequestHeader header = getRequestHeader();
		ApplicationDescription clientDesc = stackClient.getApplication();
		String serverUri = null;
		String endpointUrl = null;
		ByteString nonce = null;
		ByteString cert = null;
		Double requestedTimeout = 36000.0;
		UInteger maxRespMsgSize = uint(483822);
		CreateSessionRequest sr = new CreateSessionRequest(header, clientDesc, serverUri, endpointUrl, sessionName,
				nonce, cert, requestedTimeout, maxRespMsgSize);
		return stackClient.sendRequest(sr).whenComplete((m1, e1) -> evalCreateSession((CreateSessionResponse) m1));
	}

	/**
	 * Evaluate create session.
	 *
	 * @param msg
	 *            the message
	 */
	private void evalCreateSession(CreateSessionResponse msg) {
		sessionId = msg.getSessionId();
		authToken = msg.getAuthenticationToken();
		log.info("Authenticated as '{}' in session '{}'.", authToken, sessionId);
	}

	/**
	 * Close session.
	 *
	 * @return the completable future
	 */
	private CompletableFuture<UaResponseMessage> closeSession() {
		log.info("Closing the session...");
		RequestHeader reqHeader = getRequestHeader();
		Boolean delSubscriptions = true;
		CloseSessionRequest csr = new CloseSessionRequest(reqHeader, delSubscriptions);
		return stackClient.sendRequest(csr).whenComplete((m1, e1) -> evalCloseSession((CloseSessionResponse) m1));
	}

	/**
	 * Evaluate close session.
	 *
	 * @param msg
	 *            the message
	 */
	private void evalCloseSession(CloseSessionResponse msg) {
		StatusCode code = msg.getResponseHeader().getServiceResult();
		log.info("Session closed: {}", StatusUtils.toString(code));
		sessionId = NodeId.NULL_VALUE;
		authToken = NodeId.NULL_VALUE;
	}

	/**
	 * Activate session.
	 *
	 * @return the completable future
	 */
	private CompletableFuture<UaResponseMessage> activateSession() {
		log.info("Activating a session...");
		RequestHeader header = getRequestHeader();
		SignatureData clientSig = new SignatureData(null, null);
		SignedSoftwareCertificate[] clientSwCerts = new SignedSoftwareCertificate[0];
		String[] localeIds = new String[] { "en" };
		ExtensionObject userIdToken = useAnonymousToken ? new ExtensionObject(createAnonymousToken()) : null;
		SignatureData userTokenSig = new SignatureData(null, null);
		ActivateSessionRequest req = new ActivateSessionRequest(header, clientSig, clientSwCerts, localeIds,
				userIdToken, userTokenSig);
		return stackClient.sendRequest(req).whenComplete((m1, e1) -> evalActivateSession((ActivateSessionResponse) m1));
	}

	/**
	 * Creates the anonymous token.
	 *
	 * @return the anonymous identity token
	 */
	private AnonymousIdentityToken createAnonymousToken() {
		return new AnonymousIdentityToken("AnonymousIdentityToken");
	}

	/**
	 * Evaluate activate session.
	 *
	 * @param msg
	 *            the message
	 */
	private void evalActivateSession(ActivateSessionResponse msg) {
		StatusCode code = msg.getResponseHeader().getServiceResult();
		log.info("Session activated: {}", StatusUtils.toString(code));
	}

	/**
	 * Connect to an OPC UA server.
	 *
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
	public void connect() throws InterruptedException, ExecutionException {
		stackClient.connect().get();
		createSession().get();
		activateSession().get();
	}

	/**
	 * Reconnect again.
	 *
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
	public void reconnect() throws InterruptedException, ExecutionException {
		stackClient.connect().get();
		activateSession().get();
	}

	/**
	 * Read one node.
	 *
	 * @param nodeId
	 *            the node's id
	 * @param attribute
	 *            the node's attribute
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, DataValue>> read(NodeId nodeId, AttributeIds attribute) {
		log.info("Reading {} of node '{}'...", attribute, nodeId);
		ReadValueId[] ntr = new ReadValueId[1];
		UInteger attributeId = attribute.getIdentifier();
		String indexRange = null;
		QualifiedName dataEncoding = null;
		ntr[0] = new ReadValueId(nodeId, attributeId, indexRange, dataEncoding);
		return read(ntr);
	}

	/**
	 * Read various nodes.
	 *
	 * @param rvalIds
	 *            the ReadValueId array
	 * @return the completable future
	 */
	private CompletableFuture<Map<NodeId, DataValue>> read(ReadValueId[] rvalIds) {
		RequestHeader reqHeader = getRequestHeader();
		Double maxAge = 3600000.0;
		TimestampsToReturn ttr = TimestampsToReturn.Both;
		ReadRequest rr = new ReadRequest(reqHeader, maxAge, ttr, rvalIds);
		return stackClient.sendRequest(rr).thenApply(msg -> evalRead(rvalIds, (ReadResponse) msg));
	}

	/**
	 * Evaluate read.
	 *
	 * @param rvalIds
	 *            the ReadValueId array
	 * @param msg
	 *            the message
	 * @return the map
	 */
	private Map<NodeId, DataValue> evalRead(ReadValueId[] rvalIds, ReadResponse msg) {
		Map<NodeId, DataValue> map = new LinkedHashMap<NodeId, DataValue>();
		for (int i = 0; i < rvalIds.length; i++) {
			ReadValueId rvalId = rvalIds[i];
			DataValue dataVal = msg.getResults()[i];
			map.put(rvalId.getNodeId(), dataVal);
		}
		return map;
	}

	/**
	 * Write a node.
	 *
	 * @param nodeId
	 *            the node's id
	 * @param attribute
	 *            the node's attribute
	 * @param rawValue
	 *            the raw value
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, StatusCode>> write(NodeId nodeId, AttributeIds attribute, Object rawValue) {
		log.info("Writing value '{}' to {} of node '{}'...", rawValue, attribute, nodeId);
		WriteValue[] wvs = new WriteValue[1];
		UInteger attributeId = attribute.getIdentifier();
		String indexRange = null;
		Variant value = new Variant(rawValue);
		StatusCode code = StatusCode.GOOD;
		DateTime time = null;
		DataValue dataVal = new DataValue(value, code, time);
		wvs[0] = new WriteValue(nodeId, attributeId, indexRange, dataVal);
		return write(wvs);
	}

	/**
	 * Write to various nodes.
	 *
	 * @param wvals
	 *            the WriteValue array
	 * @return the completable future
	 */
	private CompletableFuture<Map<NodeId, StatusCode>> write(WriteValue[] wvals) {
		RequestHeader reqHeader = getRequestHeader();
		WriteRequest wr = new WriteRequest(reqHeader, wvals);
		return stackClient.sendRequest(wr).thenApply(msg -> evalWrite(wvals, (WriteResponse) msg));
	}

	/**
	 * Evaluate write.
	 *
	 * @param wvals
	 *            the WriteValue array
	 * @param msg
	 *            the message
	 * @return the map
	 */
	private Map<NodeId, StatusCode> evalWrite(WriteValue[] wvals, WriteResponse msg) {
		Map<NodeId, StatusCode> map = new LinkedHashMap<NodeId, StatusCode>();
		for (int i = 0; i < wvals.length; i++) {
			WriteValue wval = wvals[i];
			StatusCode status = msg.getResults()[i];
			map.put(wval.getNodeId(), status);
		}
		return map;
	}

	/**
	 * Browse the node.
	 *
	 * @param nodeId
	 *            the node's id
	 * @param dir
	 *            the direction
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, ReferenceDescription[]>> browse(NodeId nodeId, BrowseDirection dir) {
		log.info("Browsing node '{}'...", nodeId);
		BrowseDescription[] nodes = new BrowseDescription[1];
		NodeId refTypeId = null;
		Boolean inclSubType = false;
		UInteger nodeClassMask = uint(0);
		UInteger resMask = uint(0x111111);
		nodes[0] = new BrowseDescription(nodeId, dir, refTypeId, inclSubType, nodeClassMask, resMask);
		return browse(nodes);
	}

	/**
	 * Browse various nodes.
	 *
	 * @param nodesToBrowse
	 *            the nodes to browse
	 * @return the completable future
	 */
	private CompletableFuture<Map<NodeId, ReferenceDescription[]>> browse(BrowseDescription[] nodesToBrowse) {
		RequestHeader reqHeader = getRequestHeader();
		ViewDescription view = null;
		UInteger maxReferencesPerNode = uint(0);
		BrowseRequest br = new BrowseRequest(reqHeader, view, maxReferencesPerNode, nodesToBrowse);
		return stackClient.sendRequest(br).thenApply(msg -> evalBrowse(nodesToBrowse, (BrowseResponse) msg));
	}

	/**
	 * Evaluate browse.
	 *
	 * @param descs
	 *            the descriptions
	 * @param msg
	 *            the message
	 * @return the map
	 */
	private Map<NodeId, ReferenceDescription[]> evalBrowse(BrowseDescription[] descs, BrowseResponse msg) {
		Map<NodeId, ReferenceDescription[]> map = new LinkedHashMap<NodeId, ReferenceDescription[]>();
		for (int i = 0; i < descs.length; i++) {
			BrowseDescription desc = descs[i];
			ReferenceDescription[] result = msg.getResults()[i].getReferences();
			map.put(desc.getNodeId(), result);
		}
		return map;
	}

	/**
	 * Subscribe with interval.
	 *
	 * @param reqPubInterval
	 *            the request publication interval
	 * @return the completable future
	 */
	public CompletableFuture<UInteger> subscribe(double reqPubInterval) {
		RequestHeader reqHeader = getRequestHeader();
		UInteger reqLifetime = uint(1000);
		UInteger reqMaxKeepAlive = uint(10);
		UInteger maxNotifPerPublish = uint(0);
		Boolean publishEnabled = true;
		UByte priority = ubyte(255);
		CreateSubscriptionRequest csr = new CreateSubscriptionRequest(reqHeader, reqPubInterval, reqLifetime,
				reqMaxKeepAlive, maxNotifPerPublish, publishEnabled, priority);
		return stackClient.sendRequest(csr).thenApply(msg -> evalSubscribe((CreateSubscriptionResponse) msg));
	}

	/**
	 * Evaluate subscribe.
	 *
	 * @param msg
	 *            the message
	 * @return the u integer
	 */
	private UInteger evalSubscribe(CreateSubscriptionResponse msg) {
		// msg.getRevisedLifetimeCount();
		// msg.getRevisedMaxKeepAliveCount();
		// msg.getRevisedPublishingInterval();
		// TODO: Work with those values!
		return msg.getSubscriptionId();
	}

	/**
	 * Unsubscribe.
	 *
	 * @param subscrIds
	 *            the subscription ids
	 * @return the completable future
	 */
	public CompletableFuture<Map<UInteger, StatusCode>> unsubscribe(UInteger... subscrIds) {
		RequestHeader reqHeader = getRequestHeader();
		DeleteSubscriptionsRequest dsr = new DeleteSubscriptionsRequest(reqHeader, subscrIds);
		return stackClient.sendRequest(dsr)
				.thenApply(msg -> evalUnsubscribe(subscrIds, (DeleteSubscriptionsResponse) msg));
	}

	/**
	 * Evaluate unsubscribe.
	 *
	 * @param subscrIds
	 *            the subscr ids
	 * @param msg
	 *            the msg
	 * @return the map
	 */
	private Map<UInteger, StatusCode> evalUnsubscribe(UInteger[] subscrIds, DeleteSubscriptionsResponse msg) {
		Map<UInteger, StatusCode> results = new HashMap<UInteger, StatusCode>();
		for (int i = 0; i < subscrIds.length; i++) {
			UInteger subscrId = subscrIds[i];
			StatusCode delResult = msg.getResults()[i];
			results.put(subscrId, delResult);
		}
		return results;
	}

	/**
	 * Creates a monitor for the subscription.
	 *
	 * @param subscrId
	 *            the subscription id
	 * @param nodeId
	 *            the node id
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, UInteger>> createMonitor(UInteger subscrId, NodeId nodeId) {
		log.info("Monitoring node '{}' on subscription {}...", nodeId, subscrId);
		MonitoredItemCreateRequest[] items = new MonitoredItemCreateRequest[1];
		// Read parameters
		UInteger attrId = AttributeIds.Value.getIdentifier();
		String indexRange = null;
		QualifiedName dataEncoding = null;
		ReadValueId item = new ReadValueId(nodeId, attrId, indexRange, dataEncoding);
		// Monitoring parameters
		UInteger handle = uint(clientHandle.incrementAndGet());
		clientHandleNodes.put(handle, nodeId);
		Double sampling = 1000.0;
		ExtensionObject filter = null;
		UInteger queueSize = uint(1);
		Boolean discardOldest = false;
		MonitoringParameters params = new MonitoringParameters(handle, sampling, filter, queueSize, discardOldest);
		// Create request
		MonitoringMode mode = MonitoringMode.Reporting;
		items[0] = new MonitoredItemCreateRequest(item, mode, params);
		return createMonitor(subscrId, items);
	}

	/**
	 * Creates the monitor for the subscription.
	 *
	 * @param subscrId
	 *            the subscription id
	 * @param items
	 *            the items
	 * @return the completable future
	 */
	private CompletableFuture<Map<NodeId, UInteger>> createMonitor(UInteger subscrId,
			MonitoredItemCreateRequest[] items) {
		RequestHeader reqHeader = getRequestHeader();
		TimestampsToReturn timestamps = TimestampsToReturn.Both;
		CreateMonitoredItemsRequest cmr = new CreateMonitoredItemsRequest(reqHeader, subscrId, timestamps, items);
		return stackClient.sendRequest(cmr)
				.thenApply(msg -> evalCreateMonitor(items, (CreateMonitoredItemsResponse) msg));
	}

	/**
	 * Evaluate create monitor.
	 *
	 * @param items
	 *            the items
	 * @param msg
	 *            the message
	 * @return the map
	 */
	private Map<NodeId, UInteger> evalCreateMonitor(MonitoredItemCreateRequest[] items,
			CreateMonitoredItemsResponse msg) {
		Map<NodeId, UInteger> results = new HashMap<NodeId, UInteger>();
		for (int i = 0; i < items.length; i++) {
			MonitoredItemCreateRequest item = items[i];
			NodeId itemId = item.getItemToMonitor().getNodeId();
			MonitoredItemCreateResult result = msg.getResults()[i];
			UInteger monitorId = result.getMonitoredItemId();
			results.put(itemId, monitorId);
		}
		return results;
	}

	/**
	 * Call a method on a node.
	 *
	 * @param objectId
	 *            the object's id
	 * @param methodId
	 *            the method's id
	 * @param args
	 *            the arguments
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, Variant[]>> call(NodeId objectId, NodeId methodId, Object... args) {
		log.info("Calling method '{}' on '{}'...", methodId, objectId);
		CallMethodRequest[] reqs = new CallMethodRequest[1];
		Variant[] varArgs = new Variant[args.length];
		for (int i = 0; i < varArgs.length; i++)
			varArgs[i] = new Variant(args[i]);
		reqs[0] = new CallMethodRequest(objectId, methodId, varArgs);
		return call(reqs);
	}

	/**
	 * Call various methods.
	 *
	 * @param methods
	 *            the methods
	 * @return the completable future
	 */
	private CompletableFuture<Map<NodeId, Variant[]>> call(CallMethodRequest[] methods) {
		RequestHeader reqHeader = getRequestHeader();
		CallRequest cr = new CallRequest(reqHeader, methods);
		return stackClient.sendRequest(cr).thenApply(msg -> evalCall(methods, (CallResponse) msg));
	}

	/**
	 * Evaluate call.
	 *
	 * @param methods
	 *            the methods
	 * @param msg
	 *            the message
	 * @return the map
	 */
	private Map<NodeId, Variant[]> evalCall(CallMethodRequest[] methods, CallResponse msg) {
		Map<NodeId, Variant[]> results = new HashMap<NodeId, Variant[]>();
		for (int i = 0; i < methods.length; i++) {
			CallMethodRequest method = methods[i];
			NodeId methId = method.getMethodId();
			CallMethodResult result = msg.getResults()[i];
			Variant[] varOutputs = result.getOutputArguments();
			Variant[] outputs = Arrays.copyOf(varOutputs, varOutputs.length + 1);
			outputs[outputs.length - 1] = new Variant(result.getStatusCode());
			results.put(methId, outputs);
		}
		return results;
	}

	/**
	 * Republish.
	 *
	 * @return the completable future
	 */
	public CompletableFuture<Map<NodeId, DataValue>> republish() {
		RequestHeader reqHeader = getRequestHeader();
		SubscriptionAcknowledgement[] subscrAcks = new SubscriptionAcknowledgement[0];
		PublishRequest pr = new PublishRequest(reqHeader, subscrAcks);
		return stackClient.sendRequest(pr).thenApply(msg -> evalRepublish((PublishResponse) msg));
	}

	/**
	 * Evaluate republish.
	 *
	 * @param msg
	 *            the message
	 * @return the map
	 */
	@SuppressWarnings("unused")
	private Map<NodeId, DataValue> evalRepublish(PublishResponse msg) {
		Map<NodeId, DataValue> results = new LinkedHashMap<NodeId, DataValue>();
		// Extract base stuff
		UInteger subscrId = msg.getSubscriptionId();
		NotificationMessage notif = msg.getNotificationMessage();
		// Get information pieces
		UInteger seqNr = notif.getSequenceNumber();
		DateTime pubTime = notif.getPublishTime();
		ExtensionObject[] data = notif.getNotificationData();
		// TODO: Use other variables!
		// Go for every date
		for (ExtensionObject item : data) {
			Object obj = item.getObject();
			if (!(obj instanceof DataChangeNotification))
				throw new UnsupportedOperationException(obj.getClass() + "!");
			DataChangeNotification change = (DataChangeNotification) obj;
			for (MonitoredItemNotification min : change.getMonitoredItems()) {
				NodeId key = clientHandleNodes.get(min.getClientHandle());
				results.put(key, min.getValue());
			}
		}
		return results;
	}

	/**
	 * Disconnect.
	 */
	public void disconnect() {
		try {
			closeSession().get();
		} catch (InterruptedException | ExecutionException e) {
			// Ignore session errors
		}
		try {
			stackClient.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("close", e);
		}
	}

	/**
	 * Gets the endpoint URI.
	 *
	 * @return the endpoint URI
	 */
	public URI getEndpointURI() {
		try {
			return new URI(stackClient.getEndpointUrl());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the security mode.
	 *
	 * @return the security mode
	 */
	public MessageSecurityMode getSecurityMode() {
		return endpoint.getSecurityMode();
	}

	@Override
	public void close() throws IOException {
		disconnect();
	}
}