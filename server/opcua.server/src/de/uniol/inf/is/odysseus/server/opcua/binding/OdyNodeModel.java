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
package de.uniol.inf.is.odysseus.server.opcua.binding;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.StatusCodes;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.java.JVM;
import com.xafero.turjumaan.server.java.ObjectTranslator;
import com.xafero.turjumaan.server.java.api.CallableMethod;
import com.xafero.turjumaan.server.sdk.api.INamespaceResolver;
import com.xafero.turjumaan.server.sdk.api.INodeInfo;
import com.xafero.turjumaan.server.sdk.api.INodeModel;
import com.xafero.turjumaan.server.sdk.util.FullNodeInfo;

import de.uniol.inf.is.odysseus.server.opcua.wrappers.Data;
import de.uniol.inf.is.odysseus.server.opcua.wrappers.System;

/**
 * The Odysseus-specific node model.
 */
public class OdyNodeModel implements INodeModel, Closeable, INamespaceResolver {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(OdyNodeModel.class);

	/** The namespace id. */
	private final int namespaceId;

	/** The namespace uri. */
	private final String namespaceUri;

	/** The start id. */
	private final int startId;

	/** The nodes. */
	private final Map<NodeId, FullNodeInfo> nodes;

	/** The translator- */
	private final ObjectTranslator transl;

	/** The object references. */
	private final List<FullNodeInfo> objRefs;

	/**
	 * Instantiates a new node model.
	 *
	 * @param data
	 *            the data
	 */
	public OdyNodeModel(Data data) {
		// Some constants
		namespaceId = 1;
		namespaceUri = "http://odysseus.informatik.uni-oldenburg.de/opcua/";
		startId = 1001;
		// Set up nodes
		nodes = new LinkedHashMap<>();
		JVM dyn = new JVM();
		System sys = new System();
		// Translate them!
		transl = new ObjectTranslator(namespaceId, namespaceUri, startId, this, nodes);
		objRefs = new LinkedList<FullNodeInfo>();
		objRefs.add(transl.createNodeFrom(dyn));
		objRefs.add(transl.createNodeFrom(sys));
		objRefs.add(transl.createNodeFrom(data));
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		Iterable<ReferenceNode> items = null;
		// Inject generated stuff
		if (id.getNamespaceIndex().intValue() == 0) {
			items = new LinkedList<>();
			// Patch some references
			String idStr = id.getIdentifier() + "";
			// 0:Objects
			if (idStr.equals("85")) {
				Collection<ReferenceNode> refs = (Collection<ReferenceNode>) items;
				for (FullNodeInfo firstLevel : objRefs) {
					NodeId fli = (NodeId) firstLevel.get(AttributeIds.NodeId);
					ExpandedNodeId efli = new ExpandedNodeId(fli, getNamespaceUri(fli), getServerIndex(fli));
					refs.add(new ReferenceNode(Identifiers.Organizes, false, efli));
				}
			}
			// 0:Enumeration
			if (idStr.equals("29")) {
				Collection<ReferenceNode> enumRefs = (Collection<ReferenceNode>) items;
				for (Entry<NodeId, FullNodeInfo> e : nodes.entrySet()) {
					List<ReferenceNode> refs = e.getValue().getReferences();
					if (refs == null)
						continue;
					for (ReferenceNode r : refs) {
						if (!r.getIsInverse())
							continue;
						if (!r.getReferenceTypeId().equals(Identifiers.HasSubtype))
							continue;
						if (!r.getTargetId().toParseableString().equals("svr=0;i=29"))
							continue;
						NodeId eid = e.getKey();
						ExpandedNodeId eidx = new ExpandedNodeId(eid, getNamespaceUri(eid), getServerIndex(eid));
						enumRefs.add(new ReferenceNode(Identifiers.HasSubtype, false, eidx));
					}
				}
			}
		}
		// Work on own stuff
		if (nodes.containsKey(id)) {
			FullNodeInfo node = nodes.get(id);
			// Check if runnable is given
			Object val;
			if ((val = node.get(AttributeIds.Value)) != null)
				if (val instanceof Runnable)
					((Runnable) val).run();
			// Now get references
			if (node.getReferences() == null)
				items = Collections.emptyList();
			else if (dir == BrowseDirection.Both)
				items = node.getReferences();
			else {
				// Filter the list!
				List<ReferenceNode> copy = new LinkedList<>(node.getReferences());
				for (ReferenceNode ref : node.getReferences())
					if ((!ref.getIsInverse()) != (dir == BrowseDirection.Forward))
						copy.remove(ref);
				items = copy;
			}
		}
		return items;
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		DataValue item = null;
		if (nodes.containsKey(id)) {
			INodeInfo node = nodes.get(id);
			Object raw = node.get(attr);
			if (raw instanceof Callable<?>)
				try {
					raw = ((Callable<?>) raw).call();
				} catch (Exception e) {
					raw = e.getMessage();
				}
			if (raw instanceof Runnable || raw instanceof CallableMethod)
				// Ignore!
				raw = null;
			item = new DataValue(new Variant(raw));
		}
		return item;
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue val) {
		if (nodes.containsKey(id)) {
			INodeInfo node = nodes.get(id);
			node.set(attr, val.getValue().getValue());
		}
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		StatusCode item = new StatusCode(StatusCodes.Bad_NotFound);
		if (nodes.containsKey(meth)) {
			INodeInfo node = nodes.get(meth);
			Object raw = node.get(AttributeIds.Value);
			if (raw instanceof CallableMethod)
				try {
					((CallableMethod) raw).call(args, outs);
					item = StatusCode.GOOD;
				} catch (Exception e) {
					log.error("Failure in '" + meth + "' on '" + obj + "'!", e);
					item = new StatusCode(StatusCodes.Bad_MethodInvalid);
				}
		}
		return item;
	}

	@Override
	public void close() throws IOException {
		nodes.clear();
		objRefs.clear();
	}

	@Override
	public String getNamespaceUri(NodeId id) {
		if (id.getNamespaceIndex().intValue() == namespaceId)
			return ""; // namespaceUri;
		return null;
	}

	@Override
	public long getServerIndex(NodeId id) {
		return 0;
	}
}