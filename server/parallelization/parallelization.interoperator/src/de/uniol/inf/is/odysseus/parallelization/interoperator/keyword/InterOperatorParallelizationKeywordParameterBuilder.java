package de.uniol.inf.is.odysseus.parallelization.interoperator.keyword;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
