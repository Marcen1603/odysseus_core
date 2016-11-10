/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractGroupedOperatorTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;

/**
 * @author Dennis Nowak
 *
 */
public class GenericGroupedTransformationStrategy<T extends ILogicalOperator> extends AbstractGroupedOperatorTransformationStrategy<T> {

	public static final String NAME = "GenericGroupedTransformationStrategy";
	
	@Override
	public String getName() {
		return GenericGroupedTransformationStrategy.NAME;
	}

	@Override
	public int evaluateCompatibility(T operator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TransformationResult transform(T operator, ParallelOperatorConfiguration configurationForOperator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IParallelTransformationStrategy<T> getNewInstance() {
		return new GenericGroupedTransformationStrategy<>();
	}

}
