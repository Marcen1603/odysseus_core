package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.IMapValue;

/**
 * Base class for creating query build parameter. These parameter can be used to
 * provide additional information for building a query (e. g. special sinks as
 * default physical roots).
 * 
 * @author Wolf Bauer
 * 
 * @param <E>
 *            Defines the type of the value which could be stored in ths
 *            parameter (e. g. {@link IPhysicalOperator})
 */
public abstract class AbstractQueryBuildParameter<E> implements IMapValue<E> {
	/**
	 * The value that is stored in this parameter.
	 */
	private E value = null;

	/**
	 * Creates a new parameter.
	 * 
	 * @param value
	 *            The value of this parameter.
	 */
	protected AbstractQueryBuildParameter(E value) {
		setValue(value);
	}

	/**
	 * Sets the value of this parameter.
	 * 
	 * @param value
	 *            The new value of this parameter.
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