package de.uniol.inf.odysseus.spatiotemporal.types.point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

public class TemporalPoint implements TemporalType<GeometryWrapper> {
	
	private TemporalFunction<GeometryWrapper> function;
	
	public TemporalPoint(TemporalFunction<GeometryWrapper> function) {
		this.function = function;
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

}
