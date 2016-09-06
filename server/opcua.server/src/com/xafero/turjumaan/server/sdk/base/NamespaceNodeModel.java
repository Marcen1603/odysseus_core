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
package com.xafero.turjumaan.server.sdk.base;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;
import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

/**
 * The name space node model.
 */
public class NamespaceNodeModel implements INodeModel, Closeable {

	/** The model per name space. */
	private final Map<Integer, INodeModel> modelPerNs;

	/**
	 * Instantiates a new name space node model.
	 */
	public NamespaceNodeModel() {
		modelPerNs = new HashMap<Integer, INodeModel>();
	}

	/**
	 * Adds the node model by index.
	 *
	 * @param nsIndex
	 *            the name space index
	 * @param model
	 *            the model
	 */
	public void addNodeModel(int nsIndex, INodeModel model) {
		if (modelPerNs.containsKey(nsIndex))
			throw new UnsupportedOperationException("There's already an existing model!");
		modelPerNs.put(nsIndex, model);
	}

	/**
	 * Gets the node model by index.
	 *
	 * @param nsIndex
	 *            the name space index
	 * @return the node model
	 */
	public INodeModel getNodeModel(int nsIndex) {
		failIfNotExists(nsIndex);
		return modelPerNs.get(nsIndex);
	}

	/**
	 * Removes the node model by index.
	 *
	 * @param nsIndex
	 *            the name space index
	 * @return the node model
	 */
	public INodeModel removeNodeModel(int nsIndex) {
		failIfNotExists(nsIndex);
		return modelPerNs.remove(nsIndex);
	}

	/**
	 * Fail if not exists.
	 *
	 * @param nsIndex
	 *            the name space index
	 */
	private void failIfNotExists(int nsIndex) {
		if (!modelPerNs.containsKey(nsIndex))
			throw new UnsupportedOperationException("There's no existing model!");
	}

	/**
	 * Gets the model.
	 *
	 * @param id
	 *            the node id
	 * @return the model
	 */
	private INodeModel getModel(NodeId id) {
		return modelPerNs.get(id.getNamespaceIndex().intValue());
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		Iterable<ReferenceNode> refs = getModel(id).browse(id, dir);
		if (dir != BrowseDirection.Inverse)
			if (id.equals(Identifiers.ObjectsFolder))
				return Iterables.concat(refs, createNamespaceRefs("objects"));
			else if (id.equals(Identifiers.TypesFolder))
				return Iterables.concat(refs, createNamespaceRefs("types"));
			else if (id.equals(Identifiers.ViewsFolder))
				return Iterables.concat(refs, createNamespaceRefs("views"));
		return refs;
	}

	/**
	 * Creates the name space references.
	 *
	 * @param rootName
	 *            the root name
	 * @return the iterable
	 */
	private Iterable<ReferenceNode> createNamespaceRefs(String rootName) {
		List<ReferenceNode> nsObjs = new LinkedList<ReferenceNode>();
		for (Integer nsIdx : modelPerNs.keySet())
			if (nsIdx != 0)
				nsObjs.add(new ReferenceNode(Identifiers.Organizes, false,
						new ExpandedNodeId(new NodeId(nsIdx, rootName))));
		return nsObjs;
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		try {
			return getModel(id).read(id, attr);
		} catch (Exception e) {
			throw new NamespaceReadError(id, attr, e);
		}
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		getModel(id).write(id, attr, value);
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		return getModel(meth).execute(obj, meth, args, outs);
	}

	@Override
	public void close() throws IOException {
		for (INodeModel model : modelPerNs.values())
			if (model instanceof Closeable)
				((Closeable) model).close();
		modelPerNs.clear();
	}

	/**
	 * A name space read error.
	 */
	public static class NamespaceReadError extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 7784851198471986530L;

		/**
		 * Instantiates a new name space read error.
		 *
		 * @param id
		 *            the node id
		 * @param attr
		 *            the attribute
		 * @param e
		 *            the exception
		 */
		public NamespaceReadError(NodeId id, AttributeIds attr, Exception e) {
			super(String.format("Couldn't read '%s' at '%s'!", attr, id.toParseableString()), e);
		}
	}
}