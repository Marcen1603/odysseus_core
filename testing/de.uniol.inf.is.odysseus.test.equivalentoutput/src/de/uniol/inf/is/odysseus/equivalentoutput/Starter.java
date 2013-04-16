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
package de.uniol.inf.is.odysseus.equivalentoutput;

import java.io.IOException;
import java.util.List;

import de.uniol.inf.is.odysseus.equivalentoutput.equality.EqualityCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.order.OrderCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.reader.StreamReader;
import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;
import de.uniol.inf.is.odysseus.equivalentoutput.tuple.TupleFactory;

/**
 * @author Merlin Wasmann
 * 
 */
public class Starter {

	public static void main(String[] args) {
		int check = check(args);
		switch(check) {
		case 0:
			System.out.println("Both inputs are equivalent and in order");
			break;
		case 1:
			System.err.println("Not provided enough or wrong parameters");
			break;
		case 2:
			System.err.println("One or both files were not in order");
			break;
		case 3:
			System.err.println("Both files were not equivalent");
			break;
		}
	}
	
	/**
	 * @param args: args[0] path0, args[1] path1, (args[2] delimiter)
	 * @return
	 */
	public static int check(String[] args) {
		if (!(args.length == 2 || args.length == 3)) {
			System.err.println("Please provide two input files");
			return 1;
		}
		try {
			List<String> input0Strings = StreamReader.readFile(args[0]);
			List<String> input1Strings = StreamReader.readFile(args[1]);
			
			if(args.length == 3) {
				TupleFactory.setDelimiter(args[2]);
			}
			
			List<Tuple> input0 = TupleFactory.createTuples(input0Strings);
			List<Tuple> input1 = TupleFactory.createTuples(input1Strings);
			
			if(!OrderCheck.isInOrder(input0) || !OrderCheck.isInOrder(input1)) {
				return 2;
			}
			
			if(!EqualityCheck.containEachOther(input0, input1)) {
				return 3;
			}
		} catch (IOException ex) {
			System.err.println(ex);
			return 1;
		}
		
		return 0;
	}
}
