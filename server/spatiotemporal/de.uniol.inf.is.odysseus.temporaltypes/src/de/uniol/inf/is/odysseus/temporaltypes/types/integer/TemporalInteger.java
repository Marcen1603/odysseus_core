package de.uniol.inf.is.odysseus.temporaltypes.types.integer;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * A temporal integer uses a temporal function for integers that returns a value
 * for a certain point in time instead of having one fixed value.
 * 
 * @author Tobias Brandt
 *
 */
public class TemporalInteger implements IClone, Cloneable, Serializable, TemporalType<Integer> {

	private static final long serialVersionUID = 6537783520942392777L;

	private TemporalFunction<Integer> function;

	/**
	 * 
	 * @param function
	 *            The function which is used to calculate the values
	 */
	public TemporalInteger(TemporalFunction<Integer> function) {
		this.function = function;
	}

	@Override
	public Integer getValue(PointInTime time) {
		return this.function.getValue(time);
	}

	@Override
	public Integer[] getValues(TimeInterval interval) {

		long start = interval.getStart().getMainPoint();
		long end = interval.getEnd().getMainPoint();
		// If the array would be bigger than int we would have a memory problem anyway
		int size = (int) (end - start);
		Integer[] results = new Integer[size];

		int counter = 0;
		for (long i = start; i < end; i++, counter++) {
			results[counter] = getValue(new PointInTime(i));
		}
		return results;
	}
	
	@Override
	public double getTrust(PointInTime time) {
		return 1;
	}

	@Override
	public TemporalInteger clone() {
		return new TemporalInteger(this.function);
	}

	@Override
	public String toString() {
		return "tinteger: " + this.function.toString();
	}


}
