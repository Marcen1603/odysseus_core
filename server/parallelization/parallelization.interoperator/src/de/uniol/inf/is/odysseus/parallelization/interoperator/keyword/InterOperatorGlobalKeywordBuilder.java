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
package de.uniol.inf.is.odysseus.parallelization.interoperator.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.preexecution.InterOperatorPreExecutionHandler;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class InterOperatorGlobalKeywordBuilder{

	private static String BLANK = " ";

	public static String buildParameterString(
			Integer degree, Integer buffersize, boolean allowPostOptmization,
			boolean useThreadedBuffer) {
		StringBuilder builder = new StringBuilder();
		builder.append("#" + ParallelizationPreParserKeyword.KEYWORD + BLANK);
		PreParserKeywordParameterHelper<InterOperatorGlobalKeywordParameter> helper = PreParserKeywordParameterHelper
				.newInstance(InterOperatorGlobalKeywordParameter.class);
		Map<InterOperatorGlobalKeywordParameter, String> parameters = new HashMap<InterOperatorGlobalKeywordParameter, String>();
		parameters.put(
				InterOperatorGlobalKeywordParameter.PARALLELIZATION_TYPE,
				InterOperatorPreExecutionHandler.TYPE);
		parameters.put(
				InterOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION,
				String.valueOf(degree));
		parameters.put(InterOperatorGlobalKeywordParameter.BUFFERSIZE,
				String.valueOf(buffersize));
		parameters.put(InterOperatorGlobalKeywordParameter.OPTIMIZATION,
				String.valueOf(allowPostOptmization));
		parameters.put(InterOperatorGlobalKeywordParameter.THREADEDBUFFER,
				String.valueOf(useThreadedBuffer));
		builder.append(helper.createParameterString(parameters));

		return builder.toString();
	}
}
