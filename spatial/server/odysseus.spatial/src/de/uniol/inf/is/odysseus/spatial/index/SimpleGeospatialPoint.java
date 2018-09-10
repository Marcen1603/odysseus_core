package de.uniol.inf.is.odysseus.spatial.index;

import com.eatthepath.jeospatial.GeospatialPoint;

public class SimpleGeospatialPoint implements GeospatialPoint {
	
	private double latitude;
	private double longitude;
	
	public SimpleGeospatialPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public double getLatitude() {
		return this.latitude;
	}

	@Override
	public double getLongitude() {
		return this.longitude;
	}

}
