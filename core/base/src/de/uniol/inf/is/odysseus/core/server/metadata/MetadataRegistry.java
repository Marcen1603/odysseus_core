/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.metadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.util.LoggerHelper;

public class MetadataRegistry {

	private static final String LOGGER_NAME = MetadataRegistry.class.getName();

	private static Map<Set<String>, Class<? extends IMetaAttribute>> combinedMetadataTypes = new HashMap<Set<String>, Class<? extends IMetaAttribute>>();

	@SafeVarargs
	public static void addMetadataType(
			Class<? extends IMetaAttribute> implementationType,
			Class<? extends IMetaAttribute>... combinationOfInterfaces) {
		HashSet<String> typeSet = toStringSet(combinationOfInterfaces);
		synchronized (combinedMetadataTypes) {
			if (combinedMetadataTypes.containsKey(typeSet)
					&& combinedMetadataTypes.get(typeSet) != implementationType) {
				throw new IllegalArgumentException(
						"combined metadatatype already exists");
			}
			try {
				if (implementationType.getMethod("clone", (Class<?>[]) null)
						.getDeclaringClass() != implementationType) {
					String msg = "implementation class does not declare a clone method, this will lead to runtime exceptions";
					throw new IllegalArgumentException(msg);
				}
			} catch (Exception e) {
				LoggerHelper.getInstance(LOGGER_NAME).warn("could not check wether '"
						+ implementationType.getName() + "' supports clone() method, reason:\n\t"
						+ e.getMessage());
			}
			combinedMetadataTypes.put(typeSet, implementationType);
		}
	}

	public static Class<? extends IMetaAttribute> getMetadataType(
			String... types) {
		HashSet<String> typeSet = new HashSet<String>();
		for (String typeString : types) {
			typeSet.add(typeString);
		}
		return getMetadataType(typeSet);
	}

	public static Class<? extends IMetaAttribute> getMetadataType(
			Set<String> types) {
		synchronized (combinedMetadataTypes) {
			Class<? extends IMetaAttribute> type = combinedMetadataTypes
					.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}

			return type;
		}
	}

	public static Set<Set<String>> getAvailableMetadataCombinations() {
		synchronized (combinedMetadataTypes) {
			return combinedMetadataTypes.keySet();
		}
	}

	public static void removeMetadataType(Class<? extends IMetaAttribute> type) {
		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(Collections.singleton(type));
		}
	}

	@SafeVarargs
	public static void removeCombinedMetadataType(
			Class<? extends IMetaAttribute>... combinationOf) {
		HashSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	@SafeVarargs
	private static HashSet<String> toStringSet(
			Class<? extends IMetaAttribute>... combinationOf) {
		HashSet<String> typeSet = new HashSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}
}
