package de.uniol.inf.is.odysseus.complexnumber.function;

import de.uniol.inf.is.odysseus.complexnumber.ComplexNumber;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class AsComplexNumberFunction extends
		AbstractFunction<ComplexNumber> {


	private static final long serialVersionUID = 6130071500243869339L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{SDFDatatype.OBJECT}};
	
	public AsComplexNumberFunction() {
		super("asComplexNumber", 1, accTypes, SDFComplexNumberDatatype.COMPLEX_NUMBER);
	}
	
	@Override
	public ComplexNumber getValue() {
		return getInputValue(0);
	}

}
