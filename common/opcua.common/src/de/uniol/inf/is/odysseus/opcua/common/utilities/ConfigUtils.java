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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

/**
 * Some utility methods to bridge Odysseus and standard Java stuff.
 */
public final class ConfigUtils {

	/**
	 * Instantiates a new config utils.
	 */
	private ConfigUtils() {
	}

	/**
	 * Convert Odysseus map to Java map.
	 *
	 * @param options
	 *            the options
	 * @return the map
	 */
	public static Map<Object, Object> toStdMap(OptionMap options) {
		Map<Object, Object> map = new HashMap<>();
		return toStdMap(options, map);
	}

	/**
	 * Copy Odysseus options into Java map.
	 *
	 * @param <T>
	 *            the generic type
	 * @param options
	 *            the options
	 * @param map
	 *            the map
	 * @return the finished map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Map> T toStdMap(OptionMap options, T map) {
		for (String key : toUpperCase(options.getUnreadOptions()))
			map.put(key, options.get(key));
		return map;
	}

	/**
	 * Convert Java map to Odysseus map.
	 *
	 * @param map
	 *            the map
	 * @return the option map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static OptionMap toOptMap(Map map) {
		OptionMap options = new OptionMap(map);
		return options;
	}

	/**
	 * Convert a list to upper case.
	 *
	 * @param strings
	 *            the strings
	 * @return the list
	 */
	public static List<String> toUpperCase(Collection<String> strings) {
		List<String> list = new ArrayList<>(strings.size());
		return toUpperCase(strings, list);
	}

	/**
	 * Convert to upper case and store into a collection.
	 *
	 * @param <T>
	 *            the generic type
	 * @param strings
	 *            the strings
	 * @param coll
	 *            the collection
	 * @return the finished collection
	 */
	public static <T extends Collection<String>> T toUpperCase(Iterable<String> strings, T coll) {
		for (String string : strings)
			coll.add(string.toUpperCase());
		return coll;
	}
}