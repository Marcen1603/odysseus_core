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
package de.uniol.inf.is.odysseus.equivalentoutput.order;

import java.util.List;

import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;

/**
 * This class provides functionality to check if the input stream is in the
 * right order according to the starttimestamp.
 * 
 * @author Merlin Wasmann
 * 
 */
public class OrderCheck {

	public static boolean check(List<Tuple> optimized, List<Tuple> notOptimized, boolean showDetails) {
		return isInOrder(optimized) && isInOrder(notOptimized);
	}
	
	private static boolean isInOrder(List<Tuple> tuples) {
		for(int i = 0; i < tuples.size() - 1; i++) {
			if(tuples.get(i).compareTo(tuples.get(i+1)) < 0) {
				return false;
			}
		}
		return true;
	}
}
