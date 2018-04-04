package de.uniol.inf.is.odysseus.spatial.datatype;

import java.io.Serializable;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.IClone;

public class GeoHashWrapper implements IClone, Cloneable, Serializable {

	private static final long serialVersionUID = 7758590625140193600L;
	
	private GeoHash geoHash;
	
	public GeoHashWrapper(GeoHash geoHash) {
		this.setGeoHash(geoHash);
	}

	public GeoHash getGeoHash() {
		return geoHash;
	}

	public void setGeoHash(GeoHash geoHash) {
		this.geoHash = geoHash;
	}
	
	@Override
	public String toString() {
		return geoHash.toString();
	}

	@Override
	public GeoHashWrapper clone() {
		return new GeoHashWrapper(this.geoHash);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geoHash == null) ? 0 : geoHash.hashCode());
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
		GeoHashWrapper other = (GeoHashWrapper) obj;
		if (geoHash == null) {
			if (other.geoHash != null)
				return false;
		} else if (!geoHash.equals(other.geoHash))
			return false;
		return true;
	}
	
	
	
}
