/**
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.recommendation.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


/**
 * This class gives you to an ID of type {@link Object} an ID of type
 * {@code long}.
 * 
 * <p>
 * See the method {@link ObjectIdToLongId#objectIDAsLong(String, Object)} for
 * more details.
 * 
 * @author Cornelius Ludmann
 */
public class ObjectIdToLongId {

	private static ObjectIdToLongId INSTANCE = null;

	private Map<String, BiMap<Object, Long>> objectIdMap = null;
	private Map<String, Long> lastNumericObjectId = null;

	private ObjectIdToLongId() {
		// TODO: remove
		testObjectIDAsLong();
	}

	public static synchronized ObjectIdToLongId getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ObjectIdToLongId();
		}
		return INSTANCE;
	}

	/**
	 * This method returns to the object {@code object} an unique identifier of
	 * type {@code long}. It works as follows:
	 * 
	 * <li>If {@code object} is of type {@link String} it tries to parse this
	 * string as long and returns this long on success.
	 * 
	 * <li>If {@code object} is of type {@code long} or {@code int} it returns
	 * this number.
	 * 
	 * <li>Otherwise it creates a map: If it is the first time the method is
	 * called with this object, the object will be added to the map with a new
	 * number. If this method is called with this object again, it will re-use
	 * the number.
	 * 
	 * <p>
	 * To use different kind of objects, one can gives different values as
	 * {@objectType}. The object IDs are only distinct with the
	 * same {@objectType}.
	 * 
	 * @param objectType
	 * @param object
	 * @return
	 */
	public synchronized long objectIDAsLong(final String objectType,
			final Object object) {
		if (object instanceof String) {
			// try to parse object as long
			try {
				return Long.parseLong((String) object);
			} catch (final NumberFormatException e) {
				// ignore if parsing fails
			}
		} else if (object instanceof Long) {
			return (Long) object;
		} else if (object instanceof Integer) {
			return (Integer) object;
		}

		// ID is not a number
		if (objectIdMap == null) {
			objectIdMap = new HashMap<String, BiMap<Object, Long>>();
		}

		if (!objectIdMap.containsKey(objectType)) {
			final BiMap<Object, Long> m = HashBiMap.create();
			objectIdMap.put(objectType, m);
		}

		final Map<Object, Long> oim = objectIdMap.get(objectType);

		if (oim.containsKey(object)) {
			return oim.get(object);
		} else {
			if (lastNumericObjectId == null) {
				lastNumericObjectId = new HashMap<String, Long>();
			}
			if (lastNumericObjectId.containsKey(objectType)) {
				oim.put(object, lastNumericObjectId.get(objectType) + 1);
				lastNumericObjectId.put(objectType,
						lastNumericObjectId.get(objectType) + 1);
				return lastNumericObjectId.get(objectType);
			} else {
				oim.put(object, 0L);
				lastNumericObjectId.put(objectType, 0L);
				return 0L;
			}
		}
	}

	/**
	 * This method returns the original object to the long ID if a map was
	 * created by {@link ObjectIdToLongId#objectIDAsLong(String, Object)}.
	 * Otherwise, the long value will be returned.
	 * 
	 * @param objectType
	 * @param lng
	 * @return
	 */
	public synchronized Object longAsObjectId(final String objectType,
			final long lng) {
		if (objectIdMap == null) {
			return lng;
		}
		if (!objectIdMap.containsKey(objectType)) {
			return lng;
		}
		if (!objectIdMap.get(objectType).containsValue(lng)) {
			return lng;
		}
		return objectIdMap.get(objectType).inverse().get(lng);
	}

	/**
	 * tests the method
	 * {@link MahoutRecommendationLearner#objectIDAsLong(String, Object)}
	 * 
	 * <p>
	 * TODO: move to a test class
	 */
	// @SuppressWarnings("unused")
	private void testObjectIDAsLong() {
		if (objectIdMap != null || lastNumericObjectId != null) {
			throw new IllegalStateException();
		}

		assert objectIDAsLong("a", "1") == 1L;
		assert objectIDAsLong("a", "345") == 345L;
		assert objectIDAsLong("a", 435) == 435L;
		assert objectIDAsLong("a", 675L) == 675L;
		assert objectIDAsLong("a", new Integer(672)) == 672L;
		assert objectIDAsLong("a", new Long(672)) == 672L;
		assert objectIdMap == null;
		assert lastNumericObjectId == null;

		assert objectIDAsLong("a", "Object 1") == 0L;
		assert objectIDAsLong("a", "Object 2") == 1L;
		assert objectIDAsLong("a", "Object 3") == 2L;
		assert objectIDAsLong("b", "Object 1") == 0L;
		assert objectIDAsLong("a", "Object 4") == 3L;
		assert objectIDAsLong("b", "Object 2") == 1L;
		assert objectIDAsLong("b", "Object 3") == 2L;
		assert objectIDAsLong("a", "Object 5") == 4L;

		assert objectIdMap != null;
		assert lastNumericObjectId != null;
		objectIdMap = null;
		lastNumericObjectId = null;
	}
}
