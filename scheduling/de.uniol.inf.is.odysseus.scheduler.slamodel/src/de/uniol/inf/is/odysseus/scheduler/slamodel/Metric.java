package de.uniol.inf.is.odysseus.scheduler.slamodel;

/**
 * Definition of a sla metric
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class Metric<V, U> {

	private V value;
	private U unit;
	
	public Metric(V value, U unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	public void setUnit(U unit) {
		this.unit = unit;
	}

	public U getUnit() {
		return unit;
	}
	
	/**
	 * returns true iff the defined value is a minimum that should be reached
	 * returns false iff the defined value is a maximzm that should be reached
	 * @return
	 */
	public abstract boolean valueIsMin();

}
