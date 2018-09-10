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
package de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.intraoperator.constants.IntraOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter.IntraOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.preexecution.IntraOperatorPreExecutionHandler;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

/**
 * Keyword builder for #PARALLELIZATION keyword with parameters for intra operator parallelization 
 * 
 * @author ChrisToenjesDeye
 *
 */
public class IntraOperatorGlobalKeywordBuilder {
	
	public static String buildParameterString(Integer degree, int buffersize) {
		StringBuilder builder = new StringBuilder();
		// #PARALLELIZATION keyword
		builder.append("#" + ParallelizationPreParserKeyword.KEYWORD + IntraOperatorParallelizationConstants.BLANK);
		
		// use helper to create parameters
		PreParserKeywordParameterHelper<IntraOperatorGlobalKeywordParameter> helper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorGlobalKeywordParameter.class);
		Map<IntraOperatorGlobalKeywordParameter, String> parameters = new HashMap<IntraOperatorGlobalKeywordParameter, String>();
		parameters.put(
				IntraOperatorGlobalKeywordParameter.PARALLELIZATION_TYPE,
				IntraOperatorPreExecutionHandler.TYPE);
		parameters.put(
				IntraOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION,
				String.valueOf(degree));
		parameters.put(IntraOperatorGlobalKeywordParameter.BUFFERSIZE,
				String.valueOf(buffersize));
		builder.append(helper.createParameterString(parameters));

		return builder.toString();
	}
}
