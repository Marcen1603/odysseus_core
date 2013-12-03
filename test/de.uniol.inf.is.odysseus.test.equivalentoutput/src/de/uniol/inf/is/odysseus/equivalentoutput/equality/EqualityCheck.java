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
package de.uniol.inf.is.odysseus.equivalentoutput.equality;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;

/**
 * This class checks if the both inputs are containing each other.
 * 
 * @author Merlin Wasmann
 * 
 */
public class EqualityCheck {

	private static boolean verbose = false;

	public static boolean check(List<Tuple> input0, List<Tuple> input1,
			boolean showDetails) {
		if (input0.containsAll(input1) && input1.containsAll(input0)) {
			return true;
		}
		
		List<Tuple> missing1 = detailCheck(input0, input1);
		if (!missing1.isEmpty()) {
			System.err.println(missing1.size() + " tuples are missing from not_optimized, but are present in optimized");
			if (showDetails) {
				for (Tuple missing : missing1) {
					System.err.println(missing + " missing from not_optimized");

					if (verbose) {
						System.err.println("originating from ");
						for (Tuple split : missing.split(new int[] { 5, 9, 8 }))
							System.err.println(split);
					}
				}
			}
		}
		List<Tuple> missing0 = detailCheck(input1, input0);
		if (!missing0.isEmpty()) {
			System.err.println(missing0.size() + " tuples are missing from optimized, but are present in not_optimized");
			if (showDetails) {
				for (Tuple missing : missing0) {
					System.err.println(missing + " missing from optimized");
					if (verbose) {
						System.err.println("originating from ");
						for (Tuple split : missing.split(new int[] { 5, 9, 8 }))
							System.err.println(split);
					}
				}
			}
		}
		return false;

	}

	/**
	 * counts the amount of tuples which are in input0 but not in input1
	 * 
	 * @param input0
	 * @param input1
	 */
	private static List<Tuple> detailCheck(List<Tuple> input0,
			List<Tuple> input1) {
		List<Tuple> missing = new ArrayList<Tuple>();
		for (Tuple in0 : input0) {
			if (!input1.contains(in0)) {
				missing.add(in0);
			}
		}
		return missing;
	}

	public static void setVerbose(boolean v) {
		verbose = v;
	}

	public static boolean getVerbose() {
		return verbose;
	}
}
