package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class RawTrajectory implements IRawTrajectory {

	private final List<Point> points;

	public RawTrajectory(List<Point> points) {
		this.points = points;
	}

	public List<Point> getPoints() {
		return points;
	}
	
}
