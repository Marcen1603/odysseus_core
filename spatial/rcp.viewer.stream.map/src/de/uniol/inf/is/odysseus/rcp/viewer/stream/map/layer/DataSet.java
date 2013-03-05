package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;

public class DataSet {

	private Geometry geometry = null;
	private Tuple<?> tuple = null;

	public DataSet(Geometry geometry, Tuple<?> tuple) {
		this.geometry = geometry;
		this.tuple = tuple;
	}
	
	public DataSet(Tuple<?> tuple2, int idx, int destSrid, ScreenTransformation transformation) {
		this.tuple = tuple2;
		init(idx, destSrid, transformation);
	}

	public Geometry getGeometry() {
		return geometry;
	}
	
	public Tuple<?> getTuple() {
		return tuple;
	}

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return this.geometry.getEnvelopeInternal();
	}

	public void init(int idx, int destSrid, ScreenTransformation transformation) {
		this.geometry = (Geometry) tuple.getAttribute(idx);
		if (this.geometry.getSRID() != destSrid){
			CoordinateTransform ct = transformation.getCoordinateTransform(this.geometry.getSRID(), destSrid);
			this.geometry = (Geometry) this.geometry.clone();
			for(Coordinate coord : this.geometry.getCoordinates()){
				ProjCoordinate src = new ProjCoordinate(coord.x, coord.y);
				src.z = coord.z;
				ProjCoordinate dest = new ProjCoordinate();
				ct.transform(src, dest);
				coord.x = dest.x;
				coord.y = dest.y;
				coord.z = dest.z;
			}
			this.geometry.setSRID(destSrid);
		}	
	}
}
