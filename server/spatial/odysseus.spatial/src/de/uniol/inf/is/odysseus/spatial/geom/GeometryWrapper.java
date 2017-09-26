package de.uniol.inf.is.odysseus.spatial.geom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

import de.uniol.inf.is.odysseus.core.IClone;

@JsonSerialize(using = GeometrySerializer.class)
public class GeometryWrapper implements IClone, Cloneable, Serializable {

	private static final long serialVersionUID = 461297214516089892L;

	@JsonIgnore
	private transient Geometry geometry;
	private int id;

	public GeometryWrapper(Geometry geometry) {
		this.geometry = (Geometry) geometry.clone();
	}

	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

	public void writeObject(ObjectOutputStream oos) throws IOException {
		// To be compatible with standard WKT, the ID is ignored here
		// default serialization
		oos.defaultWriteObject();

		WKTWriter2 wktWriter = new WKTWriter2();
		String wkt = wktWriter.write(this.geometry);
		oos.writeObject(wkt);
	}

	public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, ParseException {
		// default deserialization
		ois.defaultReadObject();

		WKTReader2 reader = new WKTReader2();
		String wkt = ois.readUTF();
		this.geometry = reader.read(wkt);
	}
}
