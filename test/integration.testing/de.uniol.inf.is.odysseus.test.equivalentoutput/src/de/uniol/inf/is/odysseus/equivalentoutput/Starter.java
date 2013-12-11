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
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.equivalentoutput.duplicate.DuplicateCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.enums.StatusCode;
import de.uniol.inf.is.odysseus.equivalentoutput.equality.EqualityCheck;
import de.uniol.inf.is.odysseus.equivalentoutput.interval.IntervalMerge;
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
		List<StatusCode> checks = check(args, false);
		for (StatusCode check : checks) {
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
	}

	/**
	 * @param args
	 *            : args[0] path optimized, args[1] path not optimized, (args[2] delimiter)
	 * @return
	 */
	public static List<StatusCode> check(String[] args, boolean merge) {
		List<StatusCode> codes = new ArrayList<StatusCode>();
		if (!(args.length == 2 || args.length == 3)) {
			System.err.println("Please provide two input files");
			codes.add(StatusCode.ERROR_WRONG_PARAMETERS);
			return codes;
		}
		try {
			List<String> input0Strings = StreamReader.readFile(args[0]);
			List<String> input1Strings = StreamReader.readFile(args[1]);
			
			System.out.println("Optimized " + args[0]);
			System.out.println("Not optimized " + args[1]);

			if (args.length == 3) {
				TupleFactory.setDelimiter(args[2]);
			}

			List<Tuple> input0 = TupleFactory.createTuples(input0Strings);
			List<Tuple> input1 = TupleFactory.createTuples(input1Strings);

			if (!DuplicateCheck.check(input0, input1, true)) {
				codes.add(StatusCode.ERROR_DUPLICATES);
			}

			if (!OrderCheck.check(input0, input1, true)) {
				codes.add(StatusCode.ERROR_OUT_OF_ORDER);
			}

			List<Tuple> merged0;
			List<Tuple> merged1;
			if(merge) {
				merged0 = IntervalMerge.mergeIntervals(input0);
				merged1 = IntervalMerge.mergeIntervals(input1);
			} else {
				merged0 = input0;
				merged1 = input1;
			}
			
			if (!EqualityCheck.check(merged0, merged1, true)) {
				codes.add(StatusCode.ERROR_NOT_EQUIVALENT);
			}
		} catch (IOException ex) {
			System.err.println(ex);
			codes.add(StatusCode.ERROR_WRONG_PARAMETERS);
		}

		if(codes.isEmpty()) {
			codes.add(StatusCode.EQUIVALENT_FILES);
		}
		return codes;
	}
}
