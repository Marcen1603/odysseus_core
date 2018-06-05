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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.enums.ValueRanks;
import com.xafero.turjumaan.server.rdf.RdfNodeModel;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;

/**
 * The dynamic node model.
 */
public class DynNodeModel extends RdfNodeModel implements INodeModel, Closeable {

	/** The Constant ERR. */
	private static final String ERR = "LastError";

	/** The Constant QUAL. */
	private static final String QUAL = "LastQuality";

	/** The Constant TS. */
	private static final String TS = "LastTimeStamp";

	/** The nodes. */
	private Map<NodeId, Map<String, Object>> nodes;

	/**
	 * Instantiates a new dynamic node model.
	 */
	public DynNodeModel() {
		super(null);
		nodes = new HashMap<>();
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		// Short-circuit if no user-customizing is known
		Map<String, Object> current;
		if ((current = nodes.get(id)) == null)
			return super.read(id, attr);
		// Short-circuit if attribute is not customized
		Object raw = current.get(attr.name());
		if (raw == null)
			return super.read(id, attr);
		// Get time and date
		long error = (long) current.get(ERR);
		@SuppressWarnings("unused")
		int qual = (int) current.get(QUAL);
		long time = (long) current.get(TS);
		// Retrieve user-stuff if attribute is customized
		Variant value = new Variant(raw);
		StatusCode status = new StatusCode(error);
		DateTime sourceTime = new DateTime(time);
		return new DataValue(value, status, sourceTime);
	}

	/**
	 * Send a new value into the address space.
	 *
	 * @param id
	 *            the node's id
	 * @param attr
	 *            the node's attribute
	 * @param ov
	 *            the node's value
	 */
	public void send(NodeId id, AttributeIds attr, OPCValue<?> ov) {
		if (attr != AttributeIds.Value)
			return;
		// Find node details
		Map<String, Object> node = getOrCreateNode(id);
		// Get raw value
		Object value = ov.getValue();
		node.put(attr.name(), value);
		// Determine data type and rank
		AtomicInteger rank = new AtomicInteger(-99);
		NodeId type = determineType(value, rank);
		node.put(AttributeIds.DataType.name(), type);
		node.put(AttributeIds.ValueRank.name(), rank.get());
		// Set time and date
		node.put(ERR, ov.getError());
		node.put(QUAL, ov.getQuality());
		node.put(TS, ov.getTimestamp());
	}

	/**
	 * Determine type of value.
	 *
	 * @param value
	 *            the value
	 * @param rank
	 *            the rank
	 * @return the node id
	 */
	private NodeId determineType(Object value, AtomicInteger rank) {
		String clazz = value.getClass().getName();
		switch (clazz) {
		case "java.lang.Double":
			rank.set(ValueRanks.Scalar.getValue());
			return Identifiers.Double;
		default:
			throw new UnsupportedOperationException(value + " | " + clazz);
		}
	}

	/**
	 * Gets an existing node or create one.
	 *
	 * @param id
	 *            the id
	 * @return the or create node
	 */
	private Map<String, Object> getOrCreateNode(NodeId id) {
		Map<String, Object> details;
		if (nodes.containsKey(id))
			details = nodes.get(id);
		else
			nodes.put(id, details = new HashMap<>());
		return details;
	}

	@Override
	public void close() throws IOException {
		if (nodes != null) {
			nodes.clear();
			nodes = null;
		}
		super.close();
	}
}