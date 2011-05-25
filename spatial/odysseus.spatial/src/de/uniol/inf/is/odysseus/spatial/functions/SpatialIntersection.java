package de.uniol.inf.is.odysseus.spatial.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.datatypes.SpatialDataType;

public class SpatialIntersection extends AbstractFunction{

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public Class[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			Class<?>[] accTypes = new Class<?>[1];
			accTypes[0] = SpatialDataType.class;
			return accTypes;
		}
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "SpatialIntersection";
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getReturnType() {
		return Boolean.class;
	}

}
