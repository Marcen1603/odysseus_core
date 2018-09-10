package de.uniol.inf.is.odysseus.complexnumber.function;

import de.uniol.inf.is.odysseus.complexnumber.ComplexNumber;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class CreateComplexNumberFunction extends
		AbstractFunction<ComplexNumber> {


	private static final long serialVersionUID = 6130071500243869339L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {SDFDatatype.NUMBERS,SDFDatatype.NUMBERS};
	
	public CreateComplexNumberFunction() {
		super("newComplexNumber", 2, accTypes, SDFComplexNumberDatatype.COMPLEX_NUMBER);
	}
	
	@Override
	public ComplexNumber getValue() {
		double r = getNumericalInputValue(0);
		double i = getNumericalInputValue(1);
		return new ComplexNumber(r,i);
	}

}
