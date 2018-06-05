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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.application.services.ViewServiceSet;
import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseResultMask;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseNextRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseNextResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowsePath;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseResult;
import com.inductiveautomation.opcua.stack.core.types.structured.Node;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.inductiveautomation.opcua.stack.core.types.structured.RegisterNodesRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.RegisterNodesResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.UnregisterNodesRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.UnregisterNodesResponse;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeBrowser;
import com.xafero.turjumaan.server.sdk.api.INodeReader;
import com.xafero.turjumaan.server.sdk.util.LogUtils;

/**
 * The default view service handling the address space.
 */
public class DefaultViewService implements ViewServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultViewService.class);

	/** The browser. */
	private final INodeBrowser browser;

	/** The reader. */
	private final INodeReader reader;

	/**
	 * Instantiates a new default view service.
	 *
	 * @param browser
	 *            the browser
	 * @param reader
	 *            the reader
	 */
	public DefaultViewService(INodeBrowser browser, INodeReader reader) {
		this.browser = browser;
		this.reader = reader;
	}

	@Override
	public void onBrowse(ServiceRequest<BrowseRequest, BrowseResponse> req) throws UaException {
		BrowseDescription[] ntb = req.getRequest().getNodesToBrowse();
		log.info("Browse --> {}", LogUtils.toString(ntb));
		BrowseResult[] results = new BrowseResult[ntb.length];
		int idx = 0;
		for (BrowseDescription bd : ntb) {
			NodeId id = bd.getNodeId();
			BrowseDirection dir = bd.getBrowseDirection();
			List<ReferenceDescription> refList = new LinkedList<ReferenceDescription>();
			for (ReferenceNode ref : browser.browse(id, dir)) {
				// Extract information
				ExpandedNodeId child = ref.getTargetId();
				NodeId refTypeId = ref.getReferenceTypeId();
				boolean isForward = !(ref.getIsInverse() != null && ref.getIsInverse());
				// Fetch details
				Node details = getNode(child, bd);
				ExpandedNodeId typeDef = new ExpandedNodeId(details.getTypeId());
				QualifiedName browseName = details.getBrowseName();
				LocalizedText displayName = details.getDisplayName();
				NodeClass nodeClass = details.getNodeClass();
				refList.add(new ReferenceDescription(refTypeId, isForward, child, browseName, displayName, nodeClass,
						typeDef));
			}
			StatusCode code = StatusCode.GOOD;
			ByteString contPoint = null;
			ReferenceDescription[] refs = refList.toArray(new ReferenceDescription[refList.size()]);
			results[idx++] = new BrowseResult(code, contPoint, refs);
		}
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DiagnosticInfo[] diagInfos = null;
		BrowseResponse br = new BrowseResponse(header, results, diagInfos);
		req.setResponse(br);
	}

	/**
	 * Gets the node by expanded id.
	 *
	 * @param enid
	 *            the expanded node id
	 * @param args
	 *            the arguments
	 * @return the node
	 */
	private Node getNode(ExpandedNodeId enid, BrowseDescription args) {
		NodeId id = enid.local().get();
		return getNode(id, args);
	}

	/**
	 * Gets the node by its id.
	 *
	 * @param id
	 *            the node id
	 * @param args
	 *            the arguments
	 * @return the node
	 */
	private Node getNode(NodeId id, BrowseDescription args) {
		BrowseResultMask resMask = BrowseResultMask.from(args.getResultMask().intValue());
		return getNode(id, resMask);
	}

	/**
	 * Gets the node by its id.
	 *
	 * @param id
	 *            the node id
	 * @param mask
	 *            the mask
	 * @return the node
	 */
	private Node getNode(NodeId id, BrowseResultMask mask) {
		AttributeIds[] attrs = getAttrIds(mask);
		return getNode(id, attrs);
	}

	/**
	 * Gets the node by its id.
	 *
	 * @param id
	 *            the node id
	 * @param attrs
	 *            the attributes
	 * @return the node
	 */
	private Node getNode(NodeId id, AttributeIds[] attrs) {
		Node node = new Node();
		for (AttributeIds attr : attrs) {
			DataValue dv = reader.read(id, attr);
			Object raw = dv.getValue().getValue();
			setNode(node, attr, raw);
		}
		return node;
	}

	/**
	 * Sets the node's attribute.
	 *
	 * @param node
	 *            the node
	 * @param attr
	 *            the attribute
	 * @param raw
	 *            the raw value
	 */
	private void setNode(Node node, AttributeIds attr, Object raw) {
		try {
			String name = (attr + "");
			name = "_" + name.substring(0, 1).toLowerCase() + name.substring(1);
			Field field = node.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(node, raw);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
		}
	}

	/**
	 * Gets the attribute ids.
	 *
	 * @param mask
	 *            the mask
	 * @return the attribute ids
	 */
	private AttributeIds[] getAttrIds(BrowseResultMask mask) {
		// Be nice, handle null the same way as ALL
		if (mask == null)
			mask = BrowseResultMask.All;
		// Switch over the request mask
		switch (mask) {
		case All:
			return AttributeIds.values();
		case BrowseName:
			return new AttributeIds[] { AttributeIds.BrowseName };
		case DisplayName:
			return new AttributeIds[] { AttributeIds.DisplayName };
		case NodeClass:
			return new AttributeIds[] { AttributeIds.NodeClass };
		case None:
			return new AttributeIds[0];
		default:
			throw new UnsupportedOperationException(mask + "");
		}
	}

	@Override
	public void onBrowseNext(ServiceRequest<BrowseNextRequest, BrowseNextResponse> req) throws UaException {
		ByteString[] cp = req.getRequest().getContinuationPoints();
		log.info("BrowseNext --> {}", LogUtils.toString(cp));
	}

	@Override
	public void onRegisterNodes(ServiceRequest<RegisterNodesRequest, RegisterNodesResponse> req) throws UaException {
		NodeId[] ntr = req.getRequest().getNodesToRegister();
		log.info("RegisterNodes --> {}", LogUtils.toString(ntr));
	}

	@Override
	public void onTranslateBrowsePaths(
			ServiceRequest<TranslateBrowsePathsToNodeIdsRequest, TranslateBrowsePathsToNodeIdsResponse> req)
			throws UaException {
		BrowsePath[] brp = req.getRequest().getBrowsePaths();
		log.info("TranslateBrowsePaths --> {}", LogUtils.toString(brp));
	}

	@Override
	public void onUnregisterNodes(ServiceRequest<UnregisterNodesRequest, UnregisterNodesResponse> req)
			throws UaException {
		NodeId[] ntu = req.getRequest().getNodesToUnregister();
		log.info("UnregisterNodes --> {}", LogUtils.toString(ntu));
	}
}