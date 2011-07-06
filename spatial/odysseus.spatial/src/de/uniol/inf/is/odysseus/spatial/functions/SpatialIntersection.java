package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class SpatialIntersection extends AbstractFunction<Boolean>{

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

    public static final SDFDatatype[] accTypes = new SDFDatatype[]{
    	SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE, SDFDatatype.SPATIAL_MULTI_POINT,
    	SDFDatatype.SPATIAL_MULTI_POLYGON, SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
    };
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			return accTypes;
		}
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "SpatialIntersection";
	}

	@Override
	public Boolean getValue() {
		return ((Geometry)this.getInputValue(0)).intersects((Geometry)this.getInputValue(1));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
