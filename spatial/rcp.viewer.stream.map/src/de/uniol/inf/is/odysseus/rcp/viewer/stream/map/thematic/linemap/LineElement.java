package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.linemap;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class LineElement implements Comparable<LineElement>{
	
	private Coordinate coordinate;
	private PointInTime startTime;
	
	public LineElement(Coordinate coordinate, PointInTime startTime) {
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
	public int compareTo(LineElement other) {
		return other.getStartTime().compareTo(startTime);		
	}
	
}
