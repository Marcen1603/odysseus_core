package de.uniol.inf.is.odysseus.core.sla;

/**
 * Definition of a sla metric
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class Metric<U> {
	/**
	 * the value of the Metric that should be met. it must only be set, if the
	 * scope of the sla is of type
	 * {@link de.uniol.inf.is.odysseus.slamodel.scope.Number}.
	 * otherwise the values that must be met are defined by the threshold of the
	 * service levels
	 */
	private double value;
	/**
	 * the unit of this value.
	 */
	private U unit;

	/**
	 * creates a new metric object
	 * 
	 * @param value
	 *            the value of the metric that must be met (only for scope
	 *            {@link de.uniol.inf.is.odysseus.slamodel.scope.Number}
	 *            )
	 * @param unit
	 *            the unit of the value
	 */
	public Metric(double value, U unit) {
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
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the value of this metric. can be null if the scope of the sla is
	 *         not of type
	 *         {@link de.uniol.inf.is.odysseus.slamodel.scope.Number}.
	 */
	public double getValue() {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("METRIC ").append(this.getClass().getSimpleName()).append(
				" (").append(this.value).append(", ").append(this.unit).append(
						")");
		
		return sb.toString();
	}
	
}
