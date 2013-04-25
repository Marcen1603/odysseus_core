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
package de.uniol.inf.is.odysseus.equivalentoutput.tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can produce a tuple from a string. It is not a Odysseus-*Tuple but
 * a tuple containing raw data.
 * 
 * @author Merlin Wasmann
 * 
 */
public class TupleFactory {
	
	private static String delimiter = "\\|";

	public static Tuple createTuple(String tupleString) {
		
		String[] attributeArray = tupleString.replaceAll("\\s", "").split(delimiter);
		long startTimestamp = Long.parseLong(attributeArray[attributeArray.length - 2]);
		long endTimestamp;
		if(attributeArray[attributeArray.length -1].equals("oo")) {
			endTimestamp = startTimestamp;
		} else {
			endTimestamp = Long.parseLong(attributeArray[attributeArray.length - 1]);
		}
		return new Tuple(attributeArray, startTimestamp, endTimestamp);
	}
	
	public static List<Tuple> createTuples(List<String> tupleStrings) {
		List<Tuple> tuples = new ArrayList<Tuple>();
		for(String tuple : tupleStrings) {
			tuples.add(createTuple(tuple));
		}
		return tuples;
	}
	
	public static void setDelimiter(String delimiter) {
		TupleFactory.delimiter = delimiter;
	}
}
