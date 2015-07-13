package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.ParallelOperatorSettings;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

public interface IParallelTransformationStrategy<T extends ILogicalOperator> {
	
	String getName();
	
	Class<T> getOperatorType();
	
	List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes();
	
	Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType();
	
	int evaluateCompatibility(ILogicalOperator operator);
	
	TransformationResult transform(ILogicalOperator operator, ParallelOperatorSettings settingsForOperator);
}
