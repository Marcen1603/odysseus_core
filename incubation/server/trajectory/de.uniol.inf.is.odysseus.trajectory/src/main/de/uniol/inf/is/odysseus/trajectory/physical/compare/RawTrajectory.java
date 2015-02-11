package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class RawTrajectory {

	private final String id;
	
	private final List<Point> points;
	
	private final TimeInterval interval;

	public RawTrajectory(final String id, final List<Point> points) {
		this(id, points, null);
	}
	
	public RawTrajectory(final String id, final List<Point> points, TimeInterval interval) {
		this.id = id;
		this.points = Collections.unmodifiableList(points);
		this.interval = interval;
	}

	public String getId() {
		return this.id;
	}

	public List<Point> getPoints() {
		return this.points;
	}

	public TimeInterval getInterval() {
		return this.interval;
	}
	
	
}
