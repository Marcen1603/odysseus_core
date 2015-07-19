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
package de.uniol.inf.is.odysseus.parallelization.interoperator.parameter;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class InterOperatorParallelizationKeywordParameterBuilder {

	private static final String BLANK = " ";

	public static String buildKeywordWithParameters(
			String operatorId,
			Integer degree,
			Integer buffersize,
			IParallelTransformationStrategy<? extends ILogicalOperator> parallelTransformationStrategy,
			Class<? extends AbstractFragmentAO> fragmentClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#"
				+ InterOperatorParallelizationPreParserKeyword.KEYWORD + BLANK);
		builder.append("("
				+ InterOperatorParallelizationKeywordParameter.OPERATORID
						.getName() + "=" + operatorId + ")" + BLANK);
		builder.append("("
				+ InterOperatorParallelizationKeywordParameter.DEGREE.getName()
				+ "=" + degree + ")" + BLANK);
		builder.append("("
				+ InterOperatorParallelizationKeywordParameter.BUFFERSIZE
						.getName() + "=" + buffersize + ")" + BLANK);
		builder.append("("
				+ InterOperatorParallelizationKeywordParameter.STRATEGY
						.getName() + "="
				+ parallelTransformationStrategy.getName() + ")" + BLANK);
		builder.append("("
				+ InterOperatorParallelizationKeywordParameter.FRAGMENTATION
						.getName() + "=" + fragmentClass.getSimpleName() + ")"
				+ BLANK);
		builder.append(System.lineSeparator());
		return builder.toString();
	}
}
