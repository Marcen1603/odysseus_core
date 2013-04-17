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

	public static boolean containEachOther(List<Tuple> input0,
			List<Tuple> input1) {
		if (input0.containsAll(input1) && input1.containsAll(input0)) {
			return true;
		} else {
			List<Tuple> missing1 = detailCheck(input0, input1);
			System.err
					.println(missing1.size()
							+ " tuples are missing from input1, but are present in input0");
			for (Tuple missing : missing1) {
				System.err.println(missing + " missing from input1");
			}
			List<Tuple> missing0 = detailCheck(input1, input0);
			System.err
					.println(missing0.size()
							+ " tuples are missing from input0, but are present in input1");
			for (Tuple missing : missing0) {
				System.err.println(missing + " missing from input0");
			}
			return false;
		}
	}

	/**
	 * counts the amount of tuples which are in input0 but not in input 1
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
}
