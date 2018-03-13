package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class TemporalInteger implements IClone, Cloneable, Serializable, TemporalType<Integer> {

	private static final long serialVersionUID = 6537783520942392777L;

	private IntegerFunction function;

	public TemporalInteger(IntegerFunction function) {
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
	public TemporalInteger clone() {
		return new TemporalInteger(this.function);
	}

	@Override
	public String toString() {
		return "tinteger: " + this.function.toString();
	}

}
