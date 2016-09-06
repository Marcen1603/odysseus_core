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
package com.xafero.turjumaan.server.sdk.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

/**
 * The utilities for testing.
 */
public final class TestUtils {

	/**
	 * Instantiates a new test utility.
	 */
	private TestUtils() {
	}

	/**
	 * Read a node model fully.
	 *
	 * @param <V>
	 *            the value type
	 * @param model
	 *            the model
	 * @param node
	 *            the node
	 * @param selector
	 *            the selector
	 * @return the map
	 */
	public static <V> Map<AttributeIds, V> readFully(INodeModel model, NodeId node, Function<DataValue, V> selector) {
		Map<AttributeIds, V> map = new HashMap<>();
		for (AttributeIds attr : AttributeIds.values()) {
			DataValue dv = model.read(node, attr);
			V val = selector.apply(dv);
			map.put(attr, val);
		}
		return map;
	}

	/**
	 * Read fully and unwrap.
	 *
	 * @param model
	 *            the model
	 * @param node
	 *            the node
	 * @return the map
	 */
	public static Map<AttributeIds, Object> readFullyUnwrap(INodeModel model, NodeId node) {
		return readFully(model, node, d -> d.getValue().getValue());
	}

	/**
	 * Read fully but raw.
	 *
	 * @param model
	 *            the model
	 * @param node
	 *            the node
	 * @return the map
	 */
	public static Map<AttributeIds, DataValue> readFullyRaw(INodeModel model, NodeId node) {
		return readFully(model, node, d -> d);
	}
}