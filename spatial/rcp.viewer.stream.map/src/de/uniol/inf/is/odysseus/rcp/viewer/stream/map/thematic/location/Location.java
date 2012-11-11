package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import com.vividsolutions.jts.geom.Geometry;

public class Location {
	private Geometry location;
	private Integer value;
	public Location(Geometry location, Integer value) {
		super();
		this.location = location;
		this.value = value;
	}
	public Geometry getLocation() {
		return location;
	}
	public void setLocation(Geometry location) {
		this.location = location;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
