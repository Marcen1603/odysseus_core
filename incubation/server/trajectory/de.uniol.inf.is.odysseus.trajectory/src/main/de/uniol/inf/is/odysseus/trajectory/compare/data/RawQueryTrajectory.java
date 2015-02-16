package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

public class RawQueryTrajectory implements IRawTrajectory {

	private final List<Point> points;
	
	public RawQueryTrajectory(List<Point> points) {
		this.points = points;
	}

	public List<Point> getPoints() {
		return this.points;
	}


}
