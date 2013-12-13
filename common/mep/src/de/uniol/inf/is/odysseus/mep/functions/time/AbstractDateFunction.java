package de.uniol.inf.is.odysseus.mep.functions.time;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractDateFunction extends AbstractFunction<Integer> {

	private static final long serialVersionUID = -1164225253502762180L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos >= this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): a date");
		}
		return accTypes;
	}

	
	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}
}
