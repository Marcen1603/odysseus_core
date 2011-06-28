package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SpatialIntersection extends AbstractFunction{

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			String[] accTypes = new String[7];
			accTypes[0] = "SpatialPoint";
			accTypes[1] = "SpatialMultiPoint";
			accTypes[2] = "SpatialLine";
			accTypes[3] = "SpatialMultiLine";
			accTypes[4] = "SpatialPolygon";
			accTypes[5] = "SpatialMultiPolygon";
			accTypes[6] = "Spatial";
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
	public String getReturnType() {
		return "Boolean";
	}

}
