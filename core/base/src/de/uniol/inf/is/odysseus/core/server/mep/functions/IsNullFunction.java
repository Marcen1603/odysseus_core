package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class IsNullFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 4074484016029763344L;

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "isNull";
	}

	@Override
	public Boolean getValue() {
		return getInputValue(0) == null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		// Accept all types ... TODO: Is this a problem?
        return null;
	}
}
