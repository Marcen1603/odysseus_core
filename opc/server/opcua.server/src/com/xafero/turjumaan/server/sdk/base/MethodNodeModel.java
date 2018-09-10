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
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

/**
 * This is a special node model based on the 'Decorator Pattern' adding method
 * calling abilities to an already existing model.
 */
public class MethodNodeModel implements INodeModel, Closeable {

	/** The parent. */
	private final INodeModel parent;

	/** The functions. */
	private final Map<String, BiFunction<Variant[], List<Object>, StatusCode>> funcs;

	/**
	 * Instantiates a new method node model.
	 *
	 * @param parent
	 *            the parent
	 */
	public MethodNodeModel(INodeModel parent) {
		this.parent = parent;
		funcs = new HashMap<>();
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		return parent.browse(id, dir);
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		return parent.read(id, attr);
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		parent.write(id, attr, value);
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		return funcs.get(buildKey(obj, meth)).apply(args, outs);
	}

	/**
	 * Put the function to be called.
	 *
	 * @param obj
	 *            the object
	 * @param meth
	 *            the method
	 * @param func
	 *            the function
	 */
	public void put(NodeId obj, NodeId meth, BiFunction<Variant[], List<Object>, StatusCode> func) {
		funcs.put(buildKey(obj, meth), func);
	}

	/**
	 * Builds the key for a method.
	 *
	 * @param obj
	 *            the object
	 * @param meth
	 *            the method
	 * @return the string
	 */
	private String buildKey(NodeId obj, NodeId meth) {
		return obj.toParseableString() + "#" + meth.toParseableString();
	}

	@Override
	public void close() throws IOException {
		if (!(parent instanceof Closeable))
			return;
		((Closeable) parent).close();
	}
}