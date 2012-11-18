package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import com.vividsolutions.jts.geom.Polygon;

public class Choropleth {
	private Polygon polygon;
	private Integer value;
	public Polygon getPolygon() {
		return polygon;
	}
	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Choropleth(Polygon polygon, Integer value) {
		super();
		this.polygon = polygon;
		this.value = value;
	}
}
