package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class SpatialIntersection extends AbstractFunction{

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			SDFDatatype[] accTypes = new SDFDatatype[7];
			accTypes[0] = SDFDatatype.SPATIAL_POINT;
			accTypes[1] = SDFDatatype.SPATIAL_MULTI_POINT;
			accTypes[2] = SDFDatatype.SPATIAL_LINE;
			accTypes[3] = SDFDatatype.SPATIAL_MULTI_LINE;
			accTypes[4] = SDFDatatype.SPATIAL_POLYGON;
			accTypes[5] = SDFDatatype.SPATIAL_MULTI_POLYGON;
			accTypes[6] = SDFDatatype.SPATIAL;
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
