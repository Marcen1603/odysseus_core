/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

/**
 * Helper class for fragmentations used in inter-operator parallelization 
 * 
 * @author ChrisToenjesDeye
 *
 */
public class FragmentationTypeHelper {

	/**
	 * checks if a given string is a valid fragmentation type
	 * 
	 * @param fragmentationType
	 * @return true if the string is a valid fragmentation type, else false
	 */
	public static boolean isValidFragmentationType(String fragmentationType) {
		return FragmentationTypeHelper
				.getFragmentationTypeByName(fragmentationType) != null;
	}

	/**
	 * returns a fragmentation type 
	 * 
	 * @param name of fragmentation type
	 * @return the fragmentation for the given string
	 */
	public static Class<? extends AbstractStaticFragmentAO> getFragmentationTypeByName(
			String name) {
		Map<String, Class<? extends AbstractStaticFragmentAO>> fragmentationTypes = getFragmentationTypes();
		for (String typeName : fragmentationTypes.keySet()) {
			if (typeName.equalsIgnoreCase(name)) {
				return fragmentationTypes.get(typeName);
			}
		}
		return null;
	}

	/**
	 * returns a list of allowed fragmentation types
	 * @return list of fragmentations
	 */
	public static List<String> getAllowedFragmentationTypes() {
		Map<String, Class<? extends AbstractStaticFragmentAO>> fragmentationTypes = getFragmentationTypes();
		List<String> allowedTypes = new ArrayList<String>(
				fragmentationTypes.keySet());
		return allowedTypes;
	}

	/**
	 * returns all fragment types which can be used in inter operator parallelization (need to extended manually)
	 * @return fragemnt operators
	 */
	private static Map<String, Class<? extends AbstractStaticFragmentAO>> getFragmentationTypes() {
		// maybe there is another way to detect all available fragmentation operators
		// currently only hash, range, roundRobin and shuffle fragmentations are available
		Map<String, Class<? extends AbstractStaticFragmentAO>> fragmentTypes = new HashMap<String, Class<? extends AbstractStaticFragmentAO>>();
		fragmentTypes.put(HashFragmentAO.class.getSimpleName(),
				HashFragmentAO.class);
		fragmentTypes.put(RangeFragmentAO.class.getSimpleName(),
				RangeFragmentAO.class);
		fragmentTypes.put(RoundRobinFragmentAO.class.getSimpleName(),
				RoundRobinFragmentAO.class);
		fragmentTypes.put(ShuffleFragmentAO.class.getSimpleName(),
				ShuffleFragmentAO.class);
		return fragmentTypes;
	}
}
