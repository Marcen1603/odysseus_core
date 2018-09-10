package de.uniol.inf.is.odysseus.spatial.datatype;

import ch.hsr.geohash.WGS84Point;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class SpatioTemporalQueryResult {

	private double distanceInMeters;
	private WGS84Point centerLocation;
	private WGS84Point otherLocation;
	private PointInTime meetingTime;

	public SpatioTemporalQueryResult(double distanceInMeters, WGS84Point centerLocation, WGS84Point otherLocation,
			PointInTime meetingTime) {
		super();
		this.distanceInMeters = distanceInMeters;
		this.centerLocation = centerLocation;
		this.otherLocation = otherLocation;
		this.meetingTime = meetingTime;
	}

	public double getDistanceInMeters() {

		return distanceInMeters;
	}

	public void setDistanceInMeters(double distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}

	public WGS84Point getCenterLocation() {
		return centerLocation;
	}

	public void setCenterLocation(WGS84Point centerLocation) {
		this.centerLocation = centerLocation;
	}

	public WGS84Point getOtherLocation() {
		return otherLocation;
	}

	public void setOtherLocation(WGS84Point otherLocation) {
		this.otherLocation = otherLocation;
	}

	public PointInTime getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(PointInTime meetingTime) {
		this.meetingTime = meetingTime;
	}

}
