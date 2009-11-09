package de.uniol.inf.is.odysseus.metadata.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public class MetadataRegistry {
	private static Map<Set<String>, Class<? extends IMetaAttribute>> combinedMetadataTypes = new HashMap<Set<String>, Class<? extends IMetaAttribute>>();

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
				if (implementationType.getMethod("clone", null)
						.getDeclaringClass() != implementationType) {
					throw new IllegalArgumentException(
							"implementation class does not declare a clone method, this will lead to runtime exceptions");
				}
			} catch (SecurityException e) {
				// TODO loggen
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO loggen
				e.printStackTrace();
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

	public static void removeCombinedMetadataType(
			Class<? extends IMetaAttribute>... combinationOf) {
		HashSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	private static HashSet<String> toStringSet(
			Class<? extends IMetaAttribute>... combinationOf) {
		HashSet<String> typeSet = new HashSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}
}
