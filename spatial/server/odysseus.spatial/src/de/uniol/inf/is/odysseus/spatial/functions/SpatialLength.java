package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

/**
 * Calculates the length of a Geometry. Most accurate for LineStrings and
 * MultiLineStrings. Other geometries use an estimation with an spherical earth
 * model.
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialLength extends AbstractFunction<Double> {

	private static final long serialVersionUID = -4772212259693509945L;
	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_LINE_STRING,
			SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1 };

	public SpatialLength() {
		super("SpatialLength", 1, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {

		Object inputValue = this.getInputValue(0);
		if (!(inputValue instanceof GeometryWrapper)) {
			return null;
		}

		GeometryWrapper wrapper = (GeometryWrapper) inputValue;
		Geometry geometry = wrapper.getGeometry();
		double length = 0.0;

		/*
		 * When we have LineStrings, use the more accurate method to calculate the
		 * length
		 */
		if (geometry.getGeometryType().equals(MultiLineString.class.getSimpleName())) {
			length = calculateLength((MultiLineString) geometry);
		} else if (geometry.getGeometryType().equals(LineString.class.getSimpleName())) {
			length = calculateLength((LineString) geometry);
		} else {
			/*
			 * For anything else, use this more rough estimation using a spherical earth
			 * model
			 */
			length = geometry.getLength() * (Math.PI / 180) * 6378137;
		}

		return length;
	}

	public double calculateLength(MultiLineString multiLineString) {
		double totalLength = 0;
		for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
			Geometry geometry = multiLineString.getGeometryN(i);
			if (geometry.getGeometryType().equals(LineString.class.getSimpleName())) {
				LineString lineString = (LineString) geometry;
				double length = calculateLength(lineString);
				totalLength += length;
			}
		}
		return totalLength;
	}

	/**
	 * Calculates the length of a LineString in meters
	 */
	public double calculateLength(LineString lineString) {
		double totalDistance = 0;
		Coordinate[] coordinates = lineString.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			double distance = MetricSpatialUtils.getInstance().calculateDistance(coordinates[i - 1], coordinates[i]);
			totalDistance += distance;
		}

		return totalDistance;
	}

}
