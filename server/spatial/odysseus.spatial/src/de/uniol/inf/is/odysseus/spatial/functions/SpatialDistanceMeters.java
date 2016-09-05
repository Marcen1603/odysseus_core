package de.uniol.inf.is.odysseus.spatial.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class SpatialDistanceMeters extends AbstractFunction<Double> {

	private static final long serialVersionUID = 2781229248245144554L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_POINT,
			SDFSpatialDatatype.SPATIAL_LINE_STRING, SDFSpatialDatatype.SPATIAL_POLYGON,
			SDFSpatialDatatype.SPATIAL_MULTI_POINT, SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
			SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
			SDFSpatialDatatype.SPATIAL_GEOMETRY };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { accTypes1, accTypes1 };

	public SpatialDistanceMeters() {
		super("SpatialDistanceMeters", 2, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		
		// TODO Auto-generated method stub
		return null;
	}

}
