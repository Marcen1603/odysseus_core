package de.uniol.inf.is.odysseus.scheduler.slamodel;

/**
 * Definition of a sla metric
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class Metric<V, U> {
	/**
	 * the value of the Metric that should be met. it must only be set, if the
	 * scope of the sla is of type
	 * {@link de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number}.
	 * otherwise the values that must be met are defined by the threshold of the
	 * service levels
	 */
	private V value;
	/**
	 * the unit of this value.
	 */
	private U unit;

	/**
	 * creates a new metric object
	 * 
	 * @param value
	 *            the value of the metric that must be met (only for scope
	 *            {@link de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number}
	 *            )
	 * @param unit
	 *            the unit of the value
	 */
	public Metric(V value, U unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

	/**
	 * sets the new value of the metric
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * @return the value of this metric. can be null if the scope of the sla is
	 *         not of type
	 *         {@link de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number}.
	 */
	public V getValue() {
		return value;
	}

	/**
	 * sets the unit of the metric
	 * 
	 * @param unit
	 *            the new unit
	 */
	public void setUnit(U unit) {
		this.unit = unit;
	}

	/**
	 * @return the unit of the metric
	 */
	public U getUnit() {
		return unit;
	}

	/**
	 * specifies the 'good side' of the metric's value and the thresholds of the
	 * sla
	 * 
	 * @return true iff the defined value is a minimum that should be reached.
	 *         false iff the defined value is a maximzm that should be reached.
	 */
	public abstract boolean valueIsMin();

}
