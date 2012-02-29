package de.uniol.inf.is.odysseus.fusion.function;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequences;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import com.vividsolutions.jts.algorithm.Angle;

public class SmoothPolygon extends AbstractFunction<Geometry> {

	private static final long serialVersionUID = -3059065500918983661L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {SDFSpatialDatatype.SPATIAL_POLYGON };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		} else {
			return accTypes;
		}
	}

	@Override
	public String getSymbol() {
		return "SmoothPolygon";
	}

	boolean add = false;
	
	@Override
	public Polygon getValue() {
		Polygon polygon = (Polygon) this.getInputValue(0);		
		
		List<Coordinate> smoothCoordinates = new ArrayList<Coordinate>();
		Coordinate coordinate_last = null;
		for(Coordinate coordinate : polygon.getCoordinates()){
			if(coordinate_last != null){
				System.out.println(Angle.toDegrees(Angle.angle(coordinate_last, coordinate)));	
			}
			if(add){
 				smoothCoordinates.add(coordinate);
			}
			add = !add;
		}
		
		Coordinate[] coordinates = new Coordinate[smoothCoordinates.size()];
		MultiPoint multiPoint = polygon.getFactory().createMultiPoint(smoothCoordinates.toArray(coordinates));
		Polygon sPolygon = (Polygon)multiPoint.convexHull();
		return sPolygon;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_POLYGON;
	}

}
