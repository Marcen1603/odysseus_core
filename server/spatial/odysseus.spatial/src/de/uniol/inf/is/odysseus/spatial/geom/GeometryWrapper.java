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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeometryWrapper other = (GeometryWrapper) obj;
		if (geometry == null) {
			if (other.geometry != null)
				return false;
		} else if (!geometry.equals(other.geometry))
			return false;
		return true;
	}

}
