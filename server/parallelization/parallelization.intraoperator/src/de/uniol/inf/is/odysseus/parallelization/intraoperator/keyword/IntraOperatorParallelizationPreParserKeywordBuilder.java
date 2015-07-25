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
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.intraoperator.constants.IntraOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter.IntraOperatorParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

/**
 * Keyword builder for #INTRAOPERATOR keyword with parameters
 * 
 * @author ChrisToenjesDeye
 *
 */
public class IntraOperatorParallelizationPreParserKeywordBuilder {

	public static String buildParameterString(Integer degree, int buffersize,
			List<String> operatorIds) {
		StringBuilder builder = new StringBuilder();
		
		// add INTRAOPERATOR keyword
		builder.append("#"
				+ IntraOperatorParallelizationPreParserKeyword.KEYWORD + IntraOperatorParallelizationConstants.BLANK);
		
		// use helper to create parameters
		PreParserKeywordParameterHelper<IntraOperatorParallelizationKeywordParameter> helper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorParallelizationKeywordParameter.class);
		Map<IntraOperatorParallelizationKeywordParameter, String> parameters = new HashMap<IntraOperatorParallelizationKeywordParameter, String>();
		parameters
				.put(IntraOperatorParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION,
						String.valueOf(degree));
		parameters.put(IntraOperatorParallelizationKeywordParameter.BUFFERSIZE,
				String.valueOf(buffersize));
		String operatorIdString = operatorIds.toString().substring(1, operatorIds.toString().length()-1);
		parameters.put(IntraOperatorParallelizationKeywordParameter.OPERATORID,
				String.valueOf(operatorIdString));
		builder.append(helper.createParameterString(parameters));
		
		return builder.toString();
	}
}
