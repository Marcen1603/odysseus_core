package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import com.vividsolutions.jts.geom.Geometry;

public class Choropleth {
	private Geometry geometry;
	private Integer value;

	public Choropleth(Geometry geometry, Integer value) {
		super();
		this.geometry = geometry;
		this.value = value;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
