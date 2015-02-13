package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class RawTrajectory {

	private final String vehicleId;
	
	private final int trajectaroyId;
	
	private final List<Point> points;
	
	private final TimeInterval interval;

	public RawTrajectory(final String id, final int trajectaroyId, final List<Point> points) {
		this(id, trajectaroyId, points, null);
	}
	
	public RawTrajectory(final String id, final int trajectaroyId, final List<Point> points, TimeInterval interval) {
		this.vehicleId = id;
		this.trajectaroyId = trajectaroyId;
		this.points = Collections.unmodifiableList(points);
		this.interval = interval;
	}

	public String getVehicleId() {
		return this.vehicleId;
	}

	public int getTrajectaroyId() {
		return this.trajectaroyId;
	}

	public List<Point> getPoints() {
		return this.points;
	}

	public TimeInterval getInterval() {
		return this.interval;
	}
}
