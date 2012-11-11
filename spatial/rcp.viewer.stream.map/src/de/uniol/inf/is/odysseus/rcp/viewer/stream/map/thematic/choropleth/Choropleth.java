package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import com.vividsolutions.jts.geom.Geometry;

public class Choropleth {
	private Geometry polygon;
	private Integer value;
	public Geometry getPolygon() {
		return polygon;
	}
	public void setPolygon(Geometry polygon) {
		this.polygon = polygon;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Choropleth(Geometry polygon, Integer value) {
		super();
		this.polygon = polygon;
		this.value = value;
	}
}
