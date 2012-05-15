package de.uniol.inf.is.odysseus.spatial.functions;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ToCartesianCoordinates extends AbstractFunction<Geometry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1802751868075398723L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE } };
	private final GeometryFactory geometryFactory = new GeometryFactory();

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity()
					+ " argument(s): An array of polar coordinates.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "ToCartesianCoordinates";
	}

	@Override
	public Geometry getValue() {
		PolarCoordinate[] coordinates = (PolarCoordinate[]) this
				.getInputValue(0);

		final List<Point> points = new ArrayList<Point>(coordinates.length);
		for (PolarCoordinate coordinate : coordinates) {
			final Coordinate point = new Coordinate();
			point.x = coordinate.r * Math.cos(coordinate.a);
			point.y = coordinate.r * Math.sin(coordinate.a);
			points.add(this.geometryFactory.createPoint(point));
		}
		return this.geometryFactory.createMultiPoint(points
				.toArray(new Point[] {}));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_GEOMETRY;
	}

}
