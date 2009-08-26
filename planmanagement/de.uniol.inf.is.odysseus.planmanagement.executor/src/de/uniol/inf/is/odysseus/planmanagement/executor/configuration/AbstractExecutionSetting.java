package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

public abstract class AbstractExecutionSetting<E> implements IMapValue<E> {
	private E value = null;

	protected AbstractExecutionSetting(E value) {
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