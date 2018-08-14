package de.uniol.inf.odysseus.spatiotemporal.types.point;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;

public class TrajectoryMovingPointFunction implements TemporalFunction<GeometryWrapper> {
	
	private List<Long> timeList;
	private List<Point> points;
	
	public TrajectoryMovingPointFunction(List<Long> timeList, List<Point> points) {
		this.timeList = timeList;
		this.points = points;
	}

	@Override
	public GeometryWrapper getValue(PointInTime time) {
		
		// Search for the correct part
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
