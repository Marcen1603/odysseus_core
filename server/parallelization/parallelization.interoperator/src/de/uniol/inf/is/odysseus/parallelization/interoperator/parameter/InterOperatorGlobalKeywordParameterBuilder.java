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

import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;

public class InterOperatorGlobalKeywordParameterBuilder {

	private static String BLANK = " ";

	public static String buildInterOperatorKeywordWithParameters(
			Integer degree, Integer buffersize, boolean allowPostOptmization, boolean useThreadedBuffer) {
		StringBuilder builder = new StringBuilder();
		builder.append("#" + ParallelizationPreParserKeyword.KEYWORD + BLANK);
		builder.append("("
				+ InterOperatorGlobalKeywordParameter.PARALLELIZATION_TYPE
						.getName() + "=INTER_OPERATOR)" + BLANK);
		builder.append("("
				+ InterOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION
						.getName() + "=" + degree + ")" + BLANK);
		builder.append("("
				+ InterOperatorGlobalKeywordParameter.BUFFERSIZE.getName() + "="
				+ buffersize + ")" + BLANK);
		builder.append("("
				+ InterOperatorGlobalKeywordParameter.OPTIMIZATION.getName() + "="
				+ allowPostOptmization + ")" + BLANK);
		builder.append("("
				+ InterOperatorGlobalKeywordParameter.THREADEDBUFFER.getName() + "="
				+ String.valueOf(useThreadedBuffer) + ")" + BLANK);
		builder.append(System.lineSeparator());
		return builder.toString();
	}
}
