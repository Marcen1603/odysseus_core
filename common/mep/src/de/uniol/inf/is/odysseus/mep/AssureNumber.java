package de.uniol.inf.is.odysseus.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class AssureNumber extends AbstractFunction<Double> {

	private static final long serialVersionUID = 4773853278635916486L;
	private static final SDFDatatype[] acceptedTypes = {SDFDatatype.DOUBLE, SDFDatatype.FLOAT};

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return acceptedTypes;
	}

	@Override
	public String getSymbol() {
		return "assureNumber";
	}

	@Override
	public Double getValue() {
		Double in = getNumericalInputValue(0);
		return Double.isNaN(in)?null:in;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}
	

}
