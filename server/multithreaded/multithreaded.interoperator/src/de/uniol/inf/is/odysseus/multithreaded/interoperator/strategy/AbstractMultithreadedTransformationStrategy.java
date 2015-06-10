package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.lang.reflect.ParameterizedType;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public abstract class AbstractMultithreadedTransformationStrategy<T extends ILogicalOperator>
		implements IMultithreadedTransformationStrategy<T> {

	@SuppressWarnings("unchecked")
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

}
