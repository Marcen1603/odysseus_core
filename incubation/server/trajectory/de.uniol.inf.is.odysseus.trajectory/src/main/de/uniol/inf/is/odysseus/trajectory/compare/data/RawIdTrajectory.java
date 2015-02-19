package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class RawIdTrajectory extends RawQueryTrajectory implements IHasTextualAttributes {

	private final String vehicleId;
	
	private final int trajectoryNumber;
	
	private final Map<String, String> textualAttributes;
	
	private final ITimeInterval timeInterval;
		
	public RawIdTrajectory(List<Point> points, final String vehicleId, final int trajectoryNumber, final Map<String, String> textualAttributes,
			ITimeInterval timeInterval) {
		super(points);
		this.vehicleId = vehicleId;
		this.trajectoryNumber = trajectoryNumber;
		this.textualAttributes = textualAttributes;
		this.timeInterval = timeInterval;
	}

	public String getVehicleId() {
		return this.vehicleId;
	}


	public int getTrajectoryNumber() {
		return this.trajectoryNumber;
	}

	public ITimeInterval getTimeInterval() {
		return timeInterval;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.textualAttributes;
	}

	@Override
	public int numAttributes() {
		return this.textualAttributes.size();
	}
}
