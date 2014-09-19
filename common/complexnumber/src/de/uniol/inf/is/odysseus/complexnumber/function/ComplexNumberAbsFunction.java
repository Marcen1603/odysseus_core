package de.uniol.inf.is.odysseus.complexnumber.function;

import de.uniol.inf.is.odysseus.complexnumber.ComplexNumber;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ComplexNumberAbsFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 7115204908194856040L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{SDFComplexNumberDatatype.COMPLEX_NUMBER}};

	public ComplexNumberAbsFunction() {
		super("abs", 1, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		ComplexNumber c = (ComplexNumber) getInputValue(0);
		return c.abs();
	}

}
