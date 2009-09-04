package de.uniol.inf.is.odysseus.metadata.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.IClone;

public class MetadataRegistry {
	private static Map<Set<String>, Class<? extends IClone>> combinedMetadataTypes = new HashMap<Set<String>, Class<? extends IClone>>();

	public static void addMetadataType(Class<? extends IClone> implementationType,
			Class<? extends IClone>... combinationOfInterfaces) {
		HashSet<String> typeSet = toStringSet(combinationOfInterfaces);
		synchronized (combinedMetadataTypes) {
			if (combinedMetadataTypes.containsKey(typeSet)
					&& combinedMetadataTypes.get(typeSet) != implementationType) {
				throw new IllegalArgumentException(
						"combined metadatatype already exists");
			}
			combinedMetadataTypes.put(typeSet, implementationType);
		}
	}

	public static Class<? extends IClone> getMetadataType(String... types) {
		HashSet<String> typeSet = new HashSet<String>();
		for (String typeString : types) {
			typeSet.add(typeString);
		}
		return getMetadataType(typeSet);
	}

	public static Class<? extends IClone> getMetadataType(Set<String> types) {
		synchronized (combinedMetadataTypes) {
			Class<? extends IClone> type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}

			return type;
		}
	}

	public static void removeMetadataType(Class<? extends IClone> type) {
		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(Collections.singleton(type));
		}
	}

	public static void removeCombinedMetadataType(
			Class<? extends IClone>... combinationOf) {
		HashSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	private static HashSet<String> toStringSet(
			Class<? extends IClone>... combinationOf) {
		HashSet<String> typeSet = new HashSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}
}
