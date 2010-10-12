package de.uniol.inf.is.odysseus.planmanagement.configuration;

public class Setting<E> implements ISetting<E> {
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
	protected Setting(E value) {
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
	 * @see de.uniol.inf.is.odysseus.planmanagement.configuration.ISetting#getValue()
	 */
	@Override
	public E getValue() {
		return value;
	}
}
