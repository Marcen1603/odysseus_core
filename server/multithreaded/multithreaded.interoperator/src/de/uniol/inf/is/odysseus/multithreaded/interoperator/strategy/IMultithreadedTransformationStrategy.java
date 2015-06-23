package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public interface IMultithreadedTransformationStrategy<T extends ILogicalOperator> {
	
	String getName();
	
	Class<T> getOperatorType();
	
	List<Class<? extends AbstractFragmentAO>> getAllowedFragmentationTypes();
	
	Class<? extends AbstractFragmentAO> getPreferredFragmentationType();
	
	int evaluateCompatibility(ILogicalOperator operator);
	
	TransformationResult transform(ILogicalOperator operator, MultithreadedOperatorSettings settingsForOperator);
}
