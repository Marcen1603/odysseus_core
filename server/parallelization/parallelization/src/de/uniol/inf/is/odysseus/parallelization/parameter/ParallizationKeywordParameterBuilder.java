package de.uniol.inf.is.odysseus.parallelization.parameter;

import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;

public class ParallizationKeywordParameterBuilder {

	private static String BLANK = " ";

	public static String buildInterOperatorKeywordWithParameters(
			Integer degree, Integer buffersize, boolean allowPostOptmization) {
		StringBuilder builder = new StringBuilder();
		builder.append("#" + ParallelizationPreParserKeyword.KEYWORD + BLANK);
		builder.append("("
				+ ParallelizationKeywordParameter.PARALLELIZATION_TYPE
						.getName() + "=INTER_OPERATOR)" + BLANK);
		builder.append("("
				+ ParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION
						.getName() + "=" + degree + ")" + BLANK);
		builder.append("("
				+ ParallelizationKeywordParameter.BUFFERSIZE.getName() + "="
				+ buffersize + ")" + BLANK);
		builder.append("("
				+ ParallelizationKeywordParameter.OPTIMIZATION.getName() + "="
				+ allowPostOptmization + ")" + BLANK);
		builder.append(System.lineSeparator());
		return builder.toString();
	}
}
