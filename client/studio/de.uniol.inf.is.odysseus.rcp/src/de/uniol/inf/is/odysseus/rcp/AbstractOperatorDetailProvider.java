package de.uniol.inf.is.odysseus.rcp;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractOperatorDetailProvider<T extends IPhysicalOperator> implements IOperatorDetailProvider<T>{

	@Override
	public final Collection<Class<? extends T>> getOperatorTypes() {
		List<Class<? extends T>> classes = Lists.newArrayList();
		classes.add(getOperatorType());
		return classes;
	}
	
	protected abstract Class<? extends T> getOperatorType();
}
