package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryNumberInputFunction;

public class IsNaNFunction extends AbstractUnaryNumberInputFunction<Boolean> {

	private static final long serialVersionUID = -6023606894827478154L;

	public IsNaNFunction() {
		super("isNaN",SDFDatatype.BOOLEAN);
	}
	
	@Override
	public Boolean getValue() {
		return Double.isNaN(getNumericalInputValue(0));
	}
	

}
