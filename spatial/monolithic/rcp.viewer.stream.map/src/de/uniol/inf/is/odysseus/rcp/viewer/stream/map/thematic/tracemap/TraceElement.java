package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class TraceElement implements Comparable<TraceElement>{
	
	private Coordinate coordinate;
	private PointInTime startTime;
	
	public TraceElement(Coordinate coordinate, PointInTime startTime) {
		this.setCoordinate(coordinate);
		this.setStartTime(startTime);
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public PointInTime getStartTime() {
		return startTime;
	}

	public void setStartTime(PointInTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public int compareTo(TraceElement other) {
		return other.getStartTime().compareTo(startTime);		
	}
	
}
