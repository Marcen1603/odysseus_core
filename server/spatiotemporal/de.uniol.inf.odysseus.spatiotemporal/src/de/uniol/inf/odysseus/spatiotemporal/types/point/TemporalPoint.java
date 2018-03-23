package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * This class represents a temporal point (or, a moving point). It is a function
 * of time represented by a temporal function.
 * 
 * @author Tobias Brandt
 *
 */
public class TemporalPoint implements IClone, Cloneable, Serializable, TemporalType<GeometryWrapper> {

	private static final long serialVersionUID = 7337746723249079888L;

	private TemporalFunction<GeometryWrapper> function;

	public TemporalPoint(TemporalFunction<GeometryWrapper> function) {
		this.function = function;
	}

	public TemporalPoint(TemporalPoint other) {
		this.function = other.function;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {
		return this.function.getValue(time);
	}

	@Override
	public GeometryWrapper[] getValues(TimeInterval interval) {
		long start = interval.getStart().getMainPoint();
		long end = interval.getEnd().getMainPoint();
		// If the array would be bigger than int we would have a memory problem anyway
		int size = (int) (end - start);
		GeometryWrapper[] results = new GeometryWrapper[size];

		int counter = 0;
		for (long i = start; i < end; i++, counter++) {
			results[counter] = this.getValue(new PointInTime(i));
		}
		return results;
	}

	@Override
	public TemporalPoint clone() {
		return new TemporalPoint(this);
	}

	@Override
	public String toString() {
		return "tpoint: " + this.function.toString();
	}

}
