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

import de.uniol.inf.is.odysseus.equivalentoutput.duplicate.DuplicateCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.enums.StatusCode;
import de.uniol.inf.is.odysseus.equivalentoutput.equality.EqualityCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.order.OrderCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.reader.StreamReader;
import de.uniol.inf.is.odysseus.equivalentoutput.tuple.Tuple;
import de.uniol.inf.is.odysseus.equivalentoutput.tuple.TupleFactory;

/**
 * Testing tool to check if two output-files contain the same elements and if
 * these elements are in the correct order. To use this tool the output should
 * be done via FileSink and without the filetype-parameter. If the
 * filetype-parameter is used, important metadata is discarded.
 * 
 * @author Merlin Wasmann
 * 
 */
public class Starter {

	public static void main(String[] args) {
		StatusCode check = check(args);
		switch (check) {
		case EQUIVALENT_FILES:
			System.out.println("Both inputs are equivalent and in order");
			break;
		case ERROR_WRONG_PARAMETERS:
			System.err.println("Not provided enough or wrong parameters");
			break;
		case ERROR_OUT_OF_ORDER:
			System.err.println("One or both files were not in order");
			break;
		case ERROR_NOT_EQUIVALENT:
			System.err.println("Both files were not equivalent");
			break;
		case ERROR_DUPLICATES:
			System.err.println("One or both files contained duplicates");
			break;
		}
	}

	/**
	 * @param args
	 *            : args[0] path0, args[1] path1, (args[2] delimiter)
	 * @return
	 */
	public static StatusCode check(String[] args) {
		if (!(args.length == 2 || args.length == 3)) {
			System.err.println("Please provide two input files");
			return StatusCode.ERROR_WRONG_PARAMETERS;
		}
		try {
			List<String> input0Strings = StreamReader.readFile(args[0]);
			List<String> input1Strings = StreamReader.readFile(args[1]);

			if (args.length == 3) {
				TupleFactory.setDelimiter(args[2]);
			}

			List<Tuple> input0 = TupleFactory.createTuples(input0Strings);
			List<Tuple> input1 = TupleFactory.createTuples(input1Strings);

			if(DuplicateCheck.containsDuplicates(input0) || DuplicateCheck.containsDuplicates(input1)) {
				return StatusCode.ERROR_DUPLICATES;
			}
			
			if (!OrderCheck.isInOrder(input0) || !OrderCheck.isInOrder(input1)) {
				return StatusCode.ERROR_OUT_OF_ORDER;
			}

			if (!EqualityCheck.containEachOther(input0, input1)) {
				return StatusCode.ERROR_NOT_EQUIVALENT;
			}
		} catch (IOException ex) {
			System.err.println(ex);
			return StatusCode.ERROR_WRONG_PARAMETERS;
		}

		return StatusCode.EQUIVALENT_FILES;
	}
}
