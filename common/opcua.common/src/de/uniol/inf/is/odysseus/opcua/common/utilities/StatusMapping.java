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
package de.uniol.inf.is.odysseus.opcua.common.utilities;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.inductiveautomation.opcua.stack.core.StatusCodes;

/**
 * A helper to create status code mappings.
 */
public class StatusMapping {

	/**
	 * The kind of enumeration.
	 */
	public static enum Kind {

		/** It's bad. */
		Bad,
		/** It's good. */
		Good,
		/** Just uncertain. */
		Uncertain
	}

	/** The Constant nameToLong. */
	private static final Map<Kind, Map<String, Long>> nameToLong;

	/** The Constant longToName. */
	private static final Map<Long, Entry<Kind, String>> longToName;

	static {
		nameToLong = new HashMap<>();
		longToName = new HashMap<>();
		Field[] fields = StatusCodes.class.getFields();
		for (Field field : fields) {
			try {
				String[] parts = field.getName().split("_", 2);
				Kind kind = Kind.valueOf(parts[0]);
				String name = parts[1];
				long value = field.getLong(null);
				Map<String, Long> map;
				if (nameToLong.containsKey(kind))
					map = nameToLong.get(kind);
				else
					nameToLong.put(kind, map = new HashMap<>());
				map.put(name, value);
				longToName.put(value, new SimpleImmutableEntry<>(kind, name));
			} catch (IllegalAccessException e) {
				// Nothing here.
			}
		}
	}

	/**
	 * Gets the mapping from name to code.
	 *
	 * @return the mapping
	 */
	public static Map<Kind, Map<String, Long>> getName2Long() {
		return nameToLong;
	}

	/**
	 * Gets the mapping from code to name.
	 *
	 * @return the mapping
	 */
	public static Map<Long, Entry<Kind, String>> getLong2Name() {
		return longToName;
	}
}