package de.uniol.inf.is.odysseus.spatial.datatype;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class LocationMeasurement {

	private double latitude;
	private double longitude;
	private double horizontalDirection;
	private double speedInMetersPerSecond;
	private PointInTime measurementTime;
	private String movingObjectId;

	public LocationMeasurement(double latitude, double longitude, double horizontaldirection,
			double speedInMetersPerSecond, PointInTime measurementTime, String movingObjectId) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.horizontalDirection = horizontaldirection;
		this.setSpeedInMetersPerSecond(speedInMetersPerSecond);
		this.measurementTime = measurementTime;
		this.movingObjectId = movingObjectId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public PointInTime getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(PointInTime measurementTime) {
		this.measurementTime = measurementTime;
	}

	public String getMovingObjectId() {
		return movingObjectId;
	}

	public void setMovingObjectId(String movingObjectId) {
		this.movingObjectId = movingObjectId;
	}

	public double getHorizontalDirection() {
		return horizontalDirection;
	}

	public void setHorizontalDirection(double horizontalDirection) {
		this.horizontalDirection = horizontalDirection;
	}

	public double getSpeedInMetersPerSecond() {
		return speedInMetersPerSecond;
	}

	public void setSpeedInMetersPerSecond(double speedInMetersPerSecond) {
		this.speedInMetersPerSecond = speedInMetersPerSecond;
	}

}
