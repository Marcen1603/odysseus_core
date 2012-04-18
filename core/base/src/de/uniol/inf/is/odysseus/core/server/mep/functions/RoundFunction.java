package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class RoundFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 5571924782173674368L;

	@Override
	public int getArity() {
		return 2;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException("floor needs 2 arguments.");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "round";
	}

	@Override
	public Double getValue() {
		return Math.round(getNumericalInputValue(0)*(getNumericalInputValue(1)*10))/(getNumericalInputValue(1)*10);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
