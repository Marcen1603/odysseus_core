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
package com.xafero.turjumaan.server.ua;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint_a;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.caadiga.core.NodeModel;
import com.xafero.caadiga.model.AbstractNode;
import com.xafero.caadiga.model.Alias;
import com.xafero.caadiga.model.Reference;
import com.xafero.caadiga.model.UADataType;
import com.xafero.caadiga.model.UAMethod;
import com.xafero.caadiga.model.UANodeSet;
import com.xafero.caadiga.model.UAObject;
import com.xafero.caadiga.model.UAObjectType;
import com.xafero.caadiga.model.UAReferenceType;
import com.xafero.caadiga.model.UAVariable;
import com.xafero.caadiga.model.UAVariableType;
import com.xafero.caadiga.util.NodeUtils;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeBrowser;
import com.xafero.turjumaan.server.sdk.api.INodeInfo;
import com.xafero.turjumaan.server.sdk.util.FullNodeInfo;

/**
 * The UA NodeSet browser.
 */
public class NodeSetBrowser implements INodeBrowser, Closeable {

	/** The model. */
	private final UANodeSet model;

	/** The nodes map. */
	private final Map<String, AbstractNode> nodesMap;

	/** The node array. */
	private final String[] nodeArray;

	/** The reference array. */
	private int[][] refArray;

	/** The type array. */
	private NodeId[][] typArray;

	/**
	 * Instantiates a new NodeSet browser.
	 *
	 * @param fileName
	 *            the file name
	 */
	public NodeSetBrowser(String fileName) {
		model = NodeModel.loadFromFile(new File(fileName));
		nodesMap = NodeUtils.collectNodeIds(model);
		nodeArray = nodesMap.keySet().toArray(new String[nodesMap.size()]);
		createRefArray();
	}

	/**
	 * Creates the reference array.
	 */
	private void createRefArray() {
		refArray = new int[nodeArray.length][];
		typArray = new NodeId[nodeArray.length][];
		for (int x = 0; x < nodeArray.length; x++) {
			refArray[x] = new int[nodeArray.length];
			typArray[x] = new NodeId[nodeArray.length];
			AbstractNode nodeX = nodesMap.get(nodeArray[x]);
			for (Reference ref : nodeX.getReferences()) {
				int refIdx = Arrays.binarySearch(nodeArray, ref.getNodeId());
				if (refIdx < 0)
					continue;
				refArray[x][refIdx] = ref.isForward() ? 1 : 2;
				typArray[x][refIdx] = findNodeId(ref.getReferenceType());
			}
		}
	}

	/**
	 * Find node id by string.
	 *
	 * @param idName
	 *            the id name
	 * @return the node id
	 */
	private NodeId findNodeId(String idName) {
		try {
			Class<Identifiers> idClass = Identifiers.class;
			Field field = idClass.getField(idName);
			return (NodeId) field.get(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return NodeId.parse(idName);
		}
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId nodeId, BrowseDirection dir) {
		int idIdx = Arrays.binarySearch(nodeArray, nodeId.toParseableString());
		List<ReferenceNode> results = new LinkedList<ReferenceNode>();
		for (int i = 0; i < refArray.length; i++) {
			int val = refArray[idIdx][i];
			if (val == 0)
				continue;
			NodeId typ = typArray[idIdx][i];
			String nodeIdStr = nodeArray[i];
			if (val == 1 && (dir == BrowseDirection.Forward || dir == BrowseDirection.Both))
				results.add(new ReferenceNode(typ, false, ExpandedNodeId.parse(nodeIdStr)));
			if (val == 2 && (dir == BrowseDirection.Inverse || dir == BrowseDirection.Both))
				results.add(new ReferenceNode(typ, true, ExpandedNodeId.parse(nodeIdStr)));
		}
		for (int i = 0; i < refArray.length; i++) {
			int val = refArray[i][idIdx];
			if (val == 0)
				continue;
			NodeId typ = typArray[idIdx][i];
			String nodeIdStr = nodeArray[i];
			if (val == 1 && (dir == BrowseDirection.Inverse || dir == BrowseDirection.Both))
				results.add(new ReferenceNode(typ, true, ExpandedNodeId.parse(nodeIdStr)));
			if (val == 2 && (dir == BrowseDirection.Forward || dir == BrowseDirection.Both))
				results.add(new ReferenceNode(typ, false, ExpandedNodeId.parse(nodeIdStr)));
		}
		return results;
	}

	/**
	 * Gets the details by id.
	 *
	 * @param id
	 *            the node's id
	 * @return the details
	 */
	public INodeInfo getDetails(NodeId id) {
		AbstractNode raw = nodesMap.get(id.toParseableString());
		if (raw == null)
			return null;
		// Get node's class
		String nSimple = raw.getClass().getSimpleName().replace("UA", "");
		NodeClass nodeClass = NodeClass.valueOf(nSimple);
		// Fetch basic information
		NodeId nodeId = NodeId.parse(raw.getNodeId());
		FullNodeInfo node = new FullNodeInfo(nodeId, nodeClass, null);
		node.browseName(new QualifiedName(0, raw.getBrowseName()));
		node.desc(new LocalizedText(null, raw.getDescription()));
		node.displayName(new LocalizedText(null, raw.getDisplayName()));
		// Check if variable
		if (raw instanceof UAVariable) {
			UAVariable var = (UAVariable) raw;
			if (var.getAccessLevel() != null)
				node.accessLevel(ubyte(var.getAccessLevel()));
			node.dataType(parseNodeWithAlias(var.getDataType()));
			if (var.getMinSamplingInterval() != null)
				node.minSamplingInterval(var.getMinSamplingInterval() * 1.0);
			if (var.getUserAccessLevel() != null)
				node.userAccessLevel(ubyte(var.getUserAccessLevel()));
			node.valueRank(var.getValueRank());
			Object rawValue = var.getValue();
			if (rawValue != null)
				rawValue = ValueUtils.unpack(var.getValue(), var.getDataType(), var.getValueRank());
			node.value(rawValue);
		} else if (raw instanceof UADataType) {
			UADataType dtype = (UADataType) raw;
			node._abstract(dtype.isAbstract());
		} else if (raw instanceof UAObject) {
			UAObject obj = (UAObject) raw;
			if (obj.getEventNotifier() != null)
				node.eventNotifier(ubyte(obj.getEventNotifier()));
		} else if (raw instanceof UAVariableType) {
			UAVariableType vt = (UAVariableType) raw;
			node.valueRank(vt.getValueRank());
			node._abstract(vt.isAbstract());
			node.dataType(parseNodeWithAlias(vt.getDataType()));
			if (vt.getArrayDimensions() != null)
				node.arrayDimensions(uint_a(toIntArray(vt.getArrayDimensions())));
		} else if (raw instanceof UAReferenceType) {
			UAReferenceType rt = (UAReferenceType) raw;
			node.symmetric(rt.isSymmetric());
			node._abstract(rt.isAbstract());
			node.inverseName(new LocalizedText(null, rt.getInverseName()));
		} else if (raw instanceof UAMethod) {
			@SuppressWarnings("unused")
			UAMethod um = (UAMethod) raw;
			// TODO: Something?
		} else if (raw instanceof UAObjectType) {
			UAObjectType ot = (UAObjectType) raw;
			node._abstract(ot.isAbstract());
		} else {
			throw new UnsupportedOperationException(raw.getClass() + "");
		}
		// Set some defaults
		node.writeMask(uint(0));
		node.userWriteMask(uint(0));
		node.containsNoLoops(false);
		node.historizing(false);
		node.executable(true);
		node.userExecutable(true);
		// Check special nodes
		checkNode(node);
		return node;
	}

	/**
	 * Create string to integer array.
	 *
	 * @param dims
	 *            the dimensions
	 * @return the integer[]
	 */
	private int[] toIntArray(String dims) {
		String[] items = dims.split(",");
		int[] array = new int[items.length];
		for (int i = 0; i < items.length; i++)
			array[i] = Integer.parseInt(items[i]);
		return array;
	}

	/**
	 * Parses the node with alias.
	 *
	 * @param dataType
	 *            the data type
	 * @return the node id
	 */
	private NodeId parseNodeWithAlias(String dataType) {
		if (dataType == null)
			return null;
		for (Alias alias : model.getAliases())
			if (alias.getAlias().equals(dataType))
				return NodeId.parse(alias.getNodeId());
		return NodeId.parse(dataType);
	}

	/**
	 * Check the node.
	 *
	 * @param node
	 *            the node
	 */
	private void checkNode(FullNodeInfo node) {
		QualifiedName name = (QualifiedName) node.get(AttributeIds.BrowseName);
		switch (name.getName()) {
		/* The list of namespace URIs used by the server */
		case "NamespaceArray": // i=2255
			node.value(new String[] { "http://opcfoundation.org/UA/" });
			break;
		/* The list of server URIs used by the server */
		case "ServerArray": // i=2254
			node.value(new String[] { "http://localhost/" });
			break;
		/* The sever state (0=Running) */
		case "State":
			node.value(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void close() throws IOException {
		nodesMap.clear();
	}
}