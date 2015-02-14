package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class RawDataTrajectory extends AbstractRawTrajectory {

	private final String vehicleId;
	
	private final int trajectaroyId;
	
	private final TimeInterval interval;

	public RawDataTrajectory(final String id, final int trajectaroyId, final List<Point> points) {
		this(id, trajectaroyId, points, null);
	}
	
	public RawDataTrajectory(final String id, final int trajectaroyId, final List<Point> points, TimeInterval interval) {
		super(points);
		this.vehicleId = id;
		this.trajectaroyId = trajectaroyId;
		this.interval = interval;
	}

	public String getVehicleId() {
		return this.vehicleId;
	}

	public int getTrajectaroyId() {
		return this.trajectaroyId;
	}

	public TimeInterval getInterval() {
		return this.interval;
	}
}
