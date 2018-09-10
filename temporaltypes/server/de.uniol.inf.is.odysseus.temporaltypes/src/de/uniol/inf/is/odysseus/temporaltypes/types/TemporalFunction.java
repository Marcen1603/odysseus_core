package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * A function for a temporal type that calculates the value for a given point
 * in time.
 * 
 * @author Tobias Brandt
 *
 */
public interface TemporalFunction<T> {

	/**
	 * Calculates the value for the function for the given point in time
	 * 
	 * @param time
	 *            The point in time for which the value has to be calculated
	 * @return The value for the given point in time
	 */
	public T getValue(PointInTime time);
	
}
