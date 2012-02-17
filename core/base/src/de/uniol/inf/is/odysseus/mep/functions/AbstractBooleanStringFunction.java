package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

abstract public class AbstractBooleanStringFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 5682688277582609459L;
	public static SDFDatatype[] accTypes = new SDFDatatype[]{
		SDFDatatype.STRING};
	
	@Override
	public int getArity() {
		return 2;
	}


	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
