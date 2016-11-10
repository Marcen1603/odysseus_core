/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * @author Dennis Nowak
 *
 */
public class GenericStatelessTransformationStrategy<T extends ILogicalOperator> extends AbstractParallelTransformationStrategy<T> {

	private static final String NAME = "GenericStatelessTransformationStrategy";

	@Override
	public String getName() {
		return GenericStatelessTransformationStrategy.NAME;
	}

	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

}
