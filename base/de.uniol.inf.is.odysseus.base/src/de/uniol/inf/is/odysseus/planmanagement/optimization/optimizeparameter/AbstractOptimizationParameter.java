package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

/**
 * AbstractOptimizationParameter is the base class for all optimization parameter.
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Type of the parameter value.
 */
public abstract class AbstractOptimizationParameter<E> implements IMapValue<E> {
	/**
	 * Value of the parameter.
	 */
	private E value = null;

	/**
	 * Creates a new optimization parameter.
	 * 
	 * @param value
	 *            value of the parameter.
	 */
	protected AbstractOptimizationParameter(E value) {
		setValue(value);
	}
	
	/**
	 * Sets the value of this parameter.
	 * 
	 * @param value
	 *            new value of this parameter.
	 */
	protected void setValue(E value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue#getValue()
	 */
	@Override
	public E getValue() {
		return value;
	}
}