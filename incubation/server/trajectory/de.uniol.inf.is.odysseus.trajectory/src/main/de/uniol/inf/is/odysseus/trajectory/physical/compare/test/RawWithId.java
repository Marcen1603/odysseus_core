package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class RawWithId extends RawTrajectory implements IHasId {

	private final String vehicleId;
	
	private final int trajectoryNumber;
	
	public RawWithId(List<Point> points, final String vehicleId, final int trajectoryNumber) {
		super(points);
		this.vehicleId = vehicleId;
		this.trajectoryNumber = trajectoryNumber;
	}

	@Override
	public String getVehicleId() {
		return this.vehicleId;
	}

	@Override
	public int getTrajectoryNumber() {
		return this.trajectoryNumber;
	}

}
