package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class SpatialDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = -3990201610683127634L;

	@Override
	public int getArity() {
		return 2;
	}


    public static final SDFDatatype[] accTypes = new SDFDatatype[]{
    	SDFSpatialDatatype.SPATIAL_POINT, 
    	SDFSpatialDatatype.SPATIAL_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_POLYGON,
    	SDFSpatialDatatype.SPATIAL_MULTI_POINT,
    	SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, 
    	SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
    	SDFSpatialDatatype.SPATIAL_GEOMETRY 
	};
    
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "SpatialDistance";
	}

	@Override
	public Double getValue() {
		return ((Geometry)this.getInputValue(0)).distance((Geometry)this.getInputValue(1));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
