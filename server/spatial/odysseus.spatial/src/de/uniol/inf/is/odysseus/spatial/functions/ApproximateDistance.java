package de.uniol.inf.is.odysseus.spatial.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * A simple, but computationally cheaper approximation for the distance
 * calculation of two lat / lng spatial points. Based on
 * https://stackoverflow.com/a/15742266/3783116 and
 * http://www.movable-type.co.uk/scripts/latlong.html. Another source:
 * https://cs.nyu.edu/visual/home/proj/tiger/gisfaq.html
 * 
 * Distance will be given in meters.
 * 
 * For making the calculation more efficient, an equirectangular projection
 * (every rectangle on the earth map has the same size) is used. While haversine
 * calculation uses 7 trigonometric and 2 sqrt calculations, this only needs one
 * trigonometric (cos) and one sqrt. Nevertheless, the accuracy can be lower.
 * 
 * @author Tobias Brandt
 *
 */
public class ApproximateDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = -2820448919984979251L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_POINT },
			{ SDFSpatialDatatype.SPATIAL_POINT } };

	public ApproximateDistance() {
		super("ApproximateDistance", 2, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {

		GeometryWrapper geom1 = this.getInputValue(0);
		GeometryWrapper geom2 = this.getInputValue(1);

		double lat1 = Math.toRadians(geom1.getGeometry().getCentroid().getX());
		double lon1 = Math.toRadians(geom1.getGeometry().getCentroid().getY());

		double lat2 = Math.toRadians(geom2.getGeometry().getCentroid().getX());
		double lon2 = Math.toRadians(geom2.getGeometry().getCentroid().getY());

		double R = 6371000; // radius of the earth in m
		double x = (lon2 - lon1) * Math.cos(0.5 * (lat2 + lat1));
		double y = lat2 - lat1;
		double distance = R * Math.sqrt(x * x + y * y);

		return distance;
	}

}
