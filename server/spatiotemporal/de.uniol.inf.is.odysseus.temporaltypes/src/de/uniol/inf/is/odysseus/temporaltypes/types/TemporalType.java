package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * A value of a temporal type depends on the time instead of being a fixed
 * value.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 *            The stream element type, e.g. a tuple
 */
public interface TemporalType<T> {

	/**
	 * Returns the value of the temporal function for the given point in time
	 * 
	 * @param time
	 *            The point in time for which the value is needed
	 * @return The value of the temporal types function at the given point in time
	 */
	public T getValue(PointInTime time);

	/**
	 * Returns the value of the temporal function for a time interval with each time
	 * step from start to end as a single value in the array
	 * 
	 * @param interval
	 *            The interval from when to when the values are needed (e.g. the
	 *            ValidTime)
	 * @return An array with all values from start to end from the temporal types
	 *         function
	 */
	public T[] getValues(TimeInterval interval);

}
