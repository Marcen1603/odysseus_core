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
package de.uniol.inf.is.odysseus.parallelization.interoperator.constants;

public class InterOperatorParallelizationConstants {
	private static final String PATTERN_PAIRS = "((([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))[,])"
			+ "*(([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))";
	private static final String PATTERN_WITH_ATTRIBUTESNAMES = "([(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])([\\s][(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])*";
	private static final String PATTERN_WITHOUT_ATTRIBUTENAMES = "("
			+ PATTERN_PAIRS + ")([\\s]" + PATTERN_PAIRS + ")*";
	public static final String PATTERN_KEYWORD = PATTERN_WITH_ATTRIBUTESNAMES
			+ "|" + PATTERN_WITHOUT_ATTRIBUTENAMES;
	
	public static final int DEFAULT_BUFFER_SIZE = 10000000;
	
	public static String BLANK = " ";
}
