package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Returns the x value from a spatial point or geometry
 * @author Tobias Brandt
 *
 */
public class GetXFromSpatial extends AbstractFunction<Double> {

	private static final long serialVersionUID = 2612226805533281712L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFSpatialDatatype.SPATIAL_GEOMETRY, SDFSpatialDatatype.SPATIAL_POINT } };
	
	public GetXFromSpatial() {
		super("GetXFromSpatial", 1, accTypes, SDFDatatype.DOUBLE);
	}
	
	@Override
	public Double getValue() {
		Point point = null;
		double x = 0.0;
		if (this.getInputValue(0) instanceof Geometry) {
			point = ((Geometry) this.getInputValue(0)).getCentroid();
			x = point.getX();
		} else if (this.getInputValue(0) instanceof Point) {
			point = ((Point) this.getInputValue(0));
			x = point.getX();
		} else if (this.getInputValue(0) instanceof GeometryWrapper) {
			point = ((GeometryWrapper) this.getInputValue(0)).getGeometry().getCentroid();
			x = point.getX();
		}
		return x;
	}

}
