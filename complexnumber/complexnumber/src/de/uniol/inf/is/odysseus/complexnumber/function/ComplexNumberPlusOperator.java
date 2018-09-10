package de.uniol.inf.is.odysseus.complexnumber.function;

import de.uniol.inf.is.odysseus.complexnumber.ComplexNumber;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class ComplexNumberPlusOperator extends
		AbstractBinaryOperator<ComplexNumber> {

	private static final long serialVersionUID = -8385535716062248684L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFComplexNumberDatatype.COMPLEX_NUMBER}, {SDFComplexNumberDatatype.COMPLEX_NUMBER} };

	public ComplexNumberPlusOperator() {
		super("+", accTypes, SDFComplexNumberDatatype.COMPLEX_NUMBER);
	}
	
	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public ComplexNumber getValue() {
		ComplexNumber left = (ComplexNumber) getInputValue(0);
		ComplexNumber right = (ComplexNumber) getInputValue(1);
		return left.plus(right);
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<ComplexNumber> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<ComplexNumber> operator) {
		return false;
	}

}
