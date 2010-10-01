package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.IMapValue;

/**
 * AbstractExecutionSetting is the base class for all execution settings.
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Type of the setting value.
 */
public abstract class AbstractExecutionSetting<E> implements IMapValue<E> {
	/**
	 * Value of the setting.
	 */
	private E value = null;

	/**
	 * Creates a new execution setting.
	 * 
	 * @param value
	 *            value of the setting.
	 */
	protected AbstractExecutionSetting(E value) {
		setValue(value);
	}

	/**
	 * Sets the value of this setting.
	 * 
	 * @param value
	 *            new value of this setting.
	 */
	protected void setValue(E value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.configuration.IMapValue#getValue()
	 */
	@Override
	public E getValue() {
		return value;
	}
}