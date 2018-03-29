package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * A generic class for temporal types. As it is not possible to create a nice
 * function for every temporal type, the values are stored in a map for each
 * (necessary) point in time (from the valid time).
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class GenericTemporalType<T> implements IClone, Cloneable, Serializable, TemporalType<T> {

	private static final long serialVersionUID = -903000410292576845L;
	private SortedMap<PointInTime, T> values;

	public GenericTemporalType() {
		this.values = new TreeMap<>();
	}

	public GenericTemporalType(GenericTemporalType<T> other) {
		this.values = other.getValues();
	}

	/**
	 * Set a value for a specific point in time.
	 * 
	 * @param time
	 *            The time at which the value is valid
	 * @param value
	 *            The value
	 */
	public void setValue(PointInTime time, T value) {
		this.values.put(time, value);
	}

	@Override
	public T getValue(PointInTime time) {
		return this.values.get(time);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] getValues(TimeInterval interval) {

		PointInTime temporalDuration = interval.getEnd().minus(interval.getStart());
		if (temporalDuration.getMainPoint() > Integer.MAX_VALUE) {
			/*
			 * In case that we have more than max integer values we have a problem because
			 * calculation would take forever
			 */
			return null;
		}

		int duration = (int) (temporalDuration.getMainPoint());
		Object[] returnObject = new Object[duration];

		// Use a block to have the additional counter locally limited
		{
			int counter = 0;
			// Loop through all requested points in time
			for (PointInTime i = interval.getStart(); i.before(interval.getEnd()); i = i.plus(1)) {
				// Fill the result with all the necessary values
				returnObject[counter] = this.getValue(i);
				counter++;
			}
		}

		/*
		 * The suppress warning is necessary because it is not possible to create
		 * generic arrays.
		 */
		return (T[]) returnObject;
	}

	/**
	 * To get all values from this temporal type. This is not a copy, so don't
	 * change the values if you don't want the original values to be changed.
	 * 
	 * @return The map which stores the values.
	 */
	public SortedMap<PointInTime, T> getValues() {
		return this.values;
	}

	@Override
	public IClone clone() {
		return new GenericTemporalType<>(this);
	}

	@Override
	public String toString() {
		return "GenericTemporalType: " + this.values;
	}

}
