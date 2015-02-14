package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

public abstract class AbstractRawTrajectory implements IRawTrajectory {

	private final List<Point> points;

	public AbstractRawTrajectory(List<Point> points) {
		this.points = Collections.unmodifiableList(points);
	}

	public List<Point> getPoints() {
		return this.points;
	}
}
