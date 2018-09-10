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
package com.xafero.turjumaan.core.sdk.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;

/**
 * The identification utilities.
 */
public final class IdentUtils {

	/** The Constant aliases. */
	private static final Map<NodeId, String> aliases = getAliases();

	/**
	 * Gets the aliases.
	 *
	 * @return the aliases
	 */
	private static Map<NodeId, String> getAliases() {
		Map<NodeId, String> map = new HashMap<NodeId, String>();
		for (Field field : Identifiers.class.getFields())
			try {
				Object value = field.get(null);
				if (!(value instanceof NodeId))
					continue;
				NodeId nid = (NodeId) value;
				map.put(nid, field.getName());
			} catch (Exception e) {
				// Ignore it!
			}
		return map;
	}

	/**
	 * Instantiates a new identification utility.
	 */
	private IdentUtils() {
	}

	/**
	 * Gets the alias by id.
	 *
	 * @param id
	 *            the node's id
	 * @return the alias
	 */
	public static String getName(NodeId id) {
		return aliases.getOrDefault(id, null);
	}
}