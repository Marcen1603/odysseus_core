package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class IsNaNFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -6023606894827478154L;
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
		return "isNaN";
	}

	@Override
	public Boolean getValue() {
		return Double.isNaN(getNumericalInputValue(0));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}
	

}
