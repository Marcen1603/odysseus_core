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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.server.util.LoggerHelper;

public class MetadataRegistry {

	private static Logger logger = LoggerFactory
			.getLogger(MetadataRegistry.class);

	private static final String LOGGER_NAME = MetadataRegistry.class.getName();

	private static Map<SortedSet<String>, IMetaAttribute> combinedMetadataTypes = new HashMap<>();

	private static Map<String, IMetaAttribute> byName = new HashMap<>();

	public static void addMetadataType(IMetaAttribute type) {

		logger.trace("New Metadatatype registered " + type.getClass());

		byName.put(type.getName(), type);

		Class<? extends IMetaAttribute> implementationType = type.getClass();
		SortedSet<String> typeSet = toStringSet(type.getClasses());
		synchronized (combinedMetadataTypes) {
			if (combinedMetadataTypes.containsKey(typeSet)
					&& combinedMetadataTypes.get(typeSet).getClass() != implementationType) {
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
				LoggerHelper.getInstance(LOGGER_NAME).warn(
						"could not check wether '"
								+ implementationType.getName()
								+ "' supports clone() method, reason:\n\t"
								+ e.getMessage());
			}
			combinedMetadataTypes.put(typeSet, type);
		}
	}

	public static IMetaAttribute getMetadataType(String... types) {
		SortedSet<String> typeSet = new TreeSet<String>();
		for (String typeString : types) {
			typeSet.add(typeString);
		}
		return getMetadataType(typeSet);
	}

	public static IMetaAttribute getMetadataType(SortedSet<String> types) {
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}
			return type;
		}
	}

	public static List<SDFMetaSchema> getMetadataSchema(SortedSet<String> types) {
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}
			return type.getSchema();
		}
	}

	public static IMetaAttribute getMetadataTypeByName(String name) {
		IMetaAttribute type = byName.get(name);
		if (type == null) {
			throw new IllegalArgumentException("No metadata type for: " + name);
		}
		return type;
	}

	public static Set<SortedSet<String>> getAvailableMetadataCombinations() {
		synchronized (combinedMetadataTypes) {
			return combinedMetadataTypes.keySet();
		}
	}

	public static void removeMetadataType(IMetaAttribute type) {
		SortedSet<String> typeSet = toStringSet(type.getClasses());
		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	@SafeVarargs
	public static void removeCombinedMetadataType(
			Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	public static Set<String> getNames() {
		return byName.keySet();
	}

	@SafeVarargs
	private static SortedSet<String> toStringSet(
			Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = new TreeSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}

	// Helper methods

	public static IMetaAttribute tryCreateMetadataInstance(String parameter)
			throws IllegalArgumentException {
		try {
			return MetadataRegistry.getMetadataTypeByName(parameter).getClass()
					.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(
					"Could not create metadata of type '" + parameter + "'", e);
		}
	}

	public static SortedSet<String> toClassNames(
			List<Class<? extends IMetaAttribute>> classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}
	
	public static SortedSet<String> toClassNames(
			Class<? extends IMetaAttribute>[] classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}
}
