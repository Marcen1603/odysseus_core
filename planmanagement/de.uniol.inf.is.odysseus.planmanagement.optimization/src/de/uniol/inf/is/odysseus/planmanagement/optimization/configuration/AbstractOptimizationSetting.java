package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

/**
 * Setting for the AdvancedOptimizer.
 * 
 * @author Tobias Witt
 *
 * @param <E>
 */
public class AbstractOptimizationSetting<E> implements IMapValue<E> {
	
	private E value = null;
	
	protected AbstractOptimizationSetting(E value) {
		this.value = value;
	}

	@Override
	public E getValue() {
		return this.value;
	}
	
	protected void setValue(E value) {
		this.value = value;
	}

}
