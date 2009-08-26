package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

public abstract class AbstractQueryBuildParameter<E> implements IMapValue<E> {
	private E value = null;

	protected AbstractQueryBuildParameter(E value) {
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