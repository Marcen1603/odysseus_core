package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

public abstract class AbstractOptimizationParameter<E> implements IMapValue<E> {
	private E value = null;

	protected AbstractOptimizationParameter(E value) {
		setValue(value);
	}
	
	protected void setValue(E value) {
		this.value = value;
	}
	
	@Override
	public E getValue() {
		return value;
	}
}