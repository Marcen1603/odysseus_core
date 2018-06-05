package de.uniol.inf.is.odysseus.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class AssureNumber extends AbstractUnaryNumberInputFunction<Double> {

	private static final long serialVersionUID = 4773853278635916486L;

	public AssureNumber() {
		super("assureNumber",SDFDatatype.DOUBLE);
	}
	
	@Override
	public Double getValue() {
		Double in = getNumericalInputValue(0);
		return Double.isNaN(in)?null:in;
	}

}
