package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class RawDataTrajectory extends AbstractRawTrajectory {

	private final String vehicleId;
	
	private final int trajectaroyId;
	
	private final TimeInterval interval;

	public RawDataTrajectory(final String id, final int trajectaroyId, final List<Point> points, Map<String, String> textualAttributes) {
		this(id, trajectaroyId, points, textualAttributes, null);
	}
	
	public RawDataTrajectory(final String id, final int trajectaroyId, final List<Point> points, Map<String, String> textualAttributes, TimeInterval interval) {
		super(points, textualAttributes);
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
