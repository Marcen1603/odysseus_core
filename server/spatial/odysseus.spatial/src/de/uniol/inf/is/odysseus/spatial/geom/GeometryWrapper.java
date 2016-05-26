package de.uniol.inf.is.odysseus.spatial.geom;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.IClone;

public class GeometryWrapper implements IClone, Cloneable {

	private Geometry geometry;

	public GeometryWrapper(Geometry geometry) {
		this.geometry = (Geometry) geometry.clone();
	}
	
	public Geometry getGeometry() {
		return this.geometry;
	}
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return geometry.toString();
	}

	@Override
	public GeometryWrapper clone() {
		return new GeometryWrapper(this.geometry);
	}

}
