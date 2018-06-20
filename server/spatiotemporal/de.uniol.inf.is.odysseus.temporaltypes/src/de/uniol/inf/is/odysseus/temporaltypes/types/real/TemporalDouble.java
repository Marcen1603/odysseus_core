package de.uniol.inf.is.odysseus.temporaltypes.types.real;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

public class TemporalDouble implements IClone, Cloneable, Serializable, TemporalType<Double> {

	private static final long serialVersionUID = 4092352140046641136L;

	private TemporalFunction<Double> function;

	public TemporalDouble(TemporalFunction<Double> function) {
		this.function = function;
	}

	@Override
	public Double getValue(PointInTime time) {
		return this.function.getValue(time);
	}

	@Override
	public Double[] getValues(TimeInterval interval) {
		long start = interval.getStart().getMainPoint();
		long end = interval.getEnd().getMainPoint();
		// If the array would be bigger than int we would have a memory problem anyway
		int size = (int) (end - start);
		Double[] results = new Double[size];

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
	public IClone clone() {
		return new TemporalDouble(this.function);
	}

	@Override
	public String toString() {
		return "treal: " + this.function.toString();
	}

}
