package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class OrthodromicDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = 2781229248245144554L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_POINT,
			SDFSpatialDatatype.SPATIAL_LINE_STRING, SDFSpatialDatatype.SPATIAL_POLYGON,
			SDFSpatialDatatype.SPATIAL_MULTI_POINT, SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
			SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
			SDFSpatialDatatype.SPATIAL_GEOMETRY };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1, accTypes1 };

	public OrthodromicDistance() {
		super("OrthodromicDistance", 2, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		// TODO Use given SRID and use WGS 84 only if no SRID is given
		Geometry firstGeometry = ((GeometryWrapper) this.getInputValue(0)).getGeometry();
		Geometry secondGeometry = ((GeometryWrapper) this.getInputValue(1)).getGeometry();

		double distance = MetricSpatialUtils.getInstance().calculateDistance(firstGeometry.getCoordinate(),
				secondGeometry.getCoordinate());
		return distance;
	}

}
