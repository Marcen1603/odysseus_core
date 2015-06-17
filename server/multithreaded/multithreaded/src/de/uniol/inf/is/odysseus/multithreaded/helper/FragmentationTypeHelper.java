package de.uniol.inf.is.odysseus.multithreaded.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;

public class FragmentationTypeHelper {

	public static boolean isValidFragmentationType(String fragmentationType) {
		return FragmentationTypeHelper
				.getFragmentationTypeByName(fragmentationType) != null;
	}

	public static Class<? extends AbstractFragmentAO> getFragmentationTypeByName(
			String name) {
		Map<String, Class<? extends AbstractFragmentAO>> fragmentationTypes = getFragmentationTypes();
		for (String typeName : fragmentationTypes.keySet()) {
			if (typeName.equalsIgnoreCase(name)) {
				return fragmentationTypes.get(typeName);
			}
		}
		return null;
	}

	public static List<String> getAllowedFragmentationTypes() {
		Map<String, Class<? extends AbstractFragmentAO>> fragmentationTypes = getFragmentationTypes();
		List<String> allowedTypes = new ArrayList<String>(
				fragmentationTypes.keySet());
		return allowedTypes;
	}

	private static Map<String, Class<? extends AbstractFragmentAO>> getFragmentationTypes() {
		// TODO first try. should be more dynamically if new fragmentation types
		// are added
		Map<String, Class<? extends AbstractFragmentAO>> fragmentTypes = new HashMap<String, Class<? extends AbstractFragmentAO>>();
		fragmentTypes.put(HashFragmentAO.class.getSimpleName(),
				HashFragmentAO.class);
		fragmentTypes.put(RangeFragmentAO.class.getSimpleName(),
				RangeFragmentAO.class);
		fragmentTypes.put(RoundRobinFragmentAO.class.getSimpleName(),
				RoundRobinFragmentAO.class);
		return fragmentTypes;
	}
}
