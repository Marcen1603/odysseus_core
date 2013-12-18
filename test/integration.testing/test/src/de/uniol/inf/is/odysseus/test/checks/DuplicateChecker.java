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
package de.uniol.inf.is.odysseus.test.checks;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.set.TestSet;

/**
 * Checks if the given list contains duplicated Tuple<? extends ITimeInterval>s.
 * 
 * @author Merlin Wasmann
 * 
 */
public class DuplicateChecker implements ITestChecker {

	public static boolean check(List<Tuple<? extends ITimeInterval>> optimized, List<Tuple<? extends ITimeInterval>> notOptimized, boolean showDetails) {
		List<Tuple<? extends ITimeInterval>> dupOptimized = detailCheck(optimized);
		List<Tuple<? extends ITimeInterval>> dupNotOptimized = detailCheck(notOptimized);

		if (!dupOptimized.isEmpty()) {
			System.err.println("There were " + dupOptimized.size() + " duplicates in optimized");
			if (showDetails) {
				for (Tuple<? extends ITimeInterval> dup : dupOptimized) {
					System.err.println("Duplicate (optimized) " + dup);
				}
			}
		}

		if (!dupNotOptimized.isEmpty()) {
			System.err.println("There were " + dupNotOptimized.size() + " duplicates in notOptimized");
			if (showDetails) {
				for (Tuple<? extends ITimeInterval> dup : dupOptimized) {
					System.err.println("Duplicate (notOptimized) " + dup);
				}
			}
		}

		return dupOptimized.isEmpty() && dupNotOptimized.isEmpty();
	}

	private static List<Tuple<? extends ITimeInterval>> detailCheck(List<Tuple<? extends ITimeInterval>>  tuples) {
		List<Tuple<? extends ITimeInterval>> duplicates = new ArrayList<Tuple<? extends ITimeInterval>>();
		List<Tuple<? extends ITimeInterval>> testList = new ArrayList<Tuple<? extends ITimeInterval>>();
		testList.addAll(tuples);
		for (int i = 0; i < testList.size(); i++) {
			Tuple<? extends ITimeInterval> test = testList.get(i);
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

	@Override
	public StatusCode check(TestSet set) {
		// TODO Auto-generated method stub
		return null;
	}

}
