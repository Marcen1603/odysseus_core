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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class InterOperatorParallelizationPreParserKeywordBuilder {

	private static final String BLANK = " ";

	public static String buildKeywordWithParameters(
			String operatorId,
			String endOperatorId,
			Integer degree,
			Integer buffersize,
			IParallelTransformationStrategy<? extends ILogicalOperator> parallelTransformationStrategy,
			Class<? extends AbstractFragmentAO> fragmentClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#"
				+ InterOperatorParallelizationPreParserKeyword.KEYWORD + BLANK);

		PreParserKeywordParameterHelper<InterOperatorParallelizationKeywordParameter> helper = PreParserKeywordParameterHelper
				.newInstance(InterOperatorParallelizationKeywordParameter.class);
		Map<InterOperatorParallelizationKeywordParameter, String> parameters = new HashMap<InterOperatorParallelizationKeywordParameter, String>();
		if (!endOperatorId.isEmpty()){
			parameters.put(InterOperatorParallelizationKeywordParameter.OPERATORID,
					operatorId + ":" + endOperatorId);
		} else {
			parameters.put(InterOperatorParallelizationKeywordParameter.OPERATORID,
					operatorId);
		}
		parameters.put(InterOperatorParallelizationKeywordParameter.DEGREE,
				String.valueOf(degree));
		parameters.put(InterOperatorParallelizationKeywordParameter.BUFFERSIZE,
				String.valueOf(buffersize));
		parameters.put(InterOperatorParallelizationKeywordParameter.STRATEGY,
				parallelTransformationStrategy.getName());
		parameters.put(
				InterOperatorParallelizationKeywordParameter.FRAGMENTATION,
				fragmentClass.getSimpleName());

		builder.append(helper.createParameterString(parameters));
		return builder.toString();
	}
}