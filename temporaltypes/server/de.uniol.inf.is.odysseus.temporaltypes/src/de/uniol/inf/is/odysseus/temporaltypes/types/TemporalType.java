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

	/**
	 * The trust value can give an indicator how trustworthy a value is for a
	 * certain point in time. If the temporal type (i.e., the function) can give a
	 * value with high probability, the trust value is high, if not, its low. The
	 * trust function can, but are not obligated to, give a value between 0 and 1 as
	 * the non-temporal trust value does. Nevertheless, functions can differ from
	 * this.
	 * 
	 * @param time
	 *            The time for which the trust value shall be given
	 * @return The trust value at this time. Lower means lower trust. 1 is high
	 *         trust, 0 is low trust.
	 */
	public double getTrust(PointInTime time);

}
