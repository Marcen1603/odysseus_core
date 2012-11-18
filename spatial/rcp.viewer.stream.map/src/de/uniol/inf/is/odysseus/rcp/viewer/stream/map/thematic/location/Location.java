package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import com.vividsolutions.jts.geom.Point;

public class Location {
	private Point location;
	private Integer value;
	public Location(Point location, Integer value) {
		super();
		this.location = location;
		this.value = value;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
