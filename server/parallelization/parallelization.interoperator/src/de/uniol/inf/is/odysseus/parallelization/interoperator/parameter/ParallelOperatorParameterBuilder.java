package de.uniol.inf.is.odysseus.parallelization.interoperator.parameter;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class ParallelOperatorParameterBuilder {

	public static String buildKeywordWithParameters(
			String operatorId,
			Integer degree,
			Integer buffersize,
			IParallelTransformationStrategy<? extends ILogicalOperator> iParallelTransformationStrategy,
			Class<? extends AbstractFragmentAO> class1) {
		StringBuilder builder = new StringBuilder();

		return builder.toString();
	}
}
