/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.equivalentoutput.duplicate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;

/**
 * Checks if the given list contains duplicated tuples.
 * 
 * @author Merlin Wasmann
 * 
 */
public class DuplicateCheck {

	public static boolean check(List<Tuple> optimized,
			List<Tuple> notOptimized, boolean showDetails) {
		List<Tuple> dupOptimized = detailCheck(optimized);
		List<Tuple> dupNotOptimized = detailCheck(notOptimized);

		if (!dupOptimized.isEmpty()) {
			System.err.println("There were " + dupOptimized.size()
					+ " duplicates in optimized");
			if (showDetails) {
				for (Tuple dup : dupOptimized) {
					System.err.println("Duplicate (optimized) " + dup);
				}
			}
		}

		if (!dupNotOptimized.isEmpty()) {
			System.err.println("There were " + dupNotOptimized.size()
					+ " duplicates in notOptimized");
			if (showDetails) {
				for (Tuple dup : dupOptimized) {
					System.err.println("Duplicate (notOptimized) " + dup);
				}
			}
		}

		return dupOptimized.isEmpty() && dupNotOptimized.isEmpty();
	}

	private static List<Tuple> detailCheck(List<Tuple> tuples) {
		List<Tuple> duplicates = new ArrayList<Tuple>();
		List<Tuple> testList = new ArrayList<Tuple>();
		testList.addAll(tuples);
		for (int i = 0; i < testList.size(); i++) {
			Tuple test = testList.get(i);
			testList.remove(i);
			if (testList.contains(test)) {
				if (!duplicates.contains(test)) {
					duplicates.add(test);
				}
			}
			testList.add(i, test);
		}
		return duplicates;
	}

}
