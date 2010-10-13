package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class MultiplicationOperator extends AbstractBinaryOperator<Double> {

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public String getSymbol() {
		return "*";
	}

	@Override
	public Double getValue() {
		return getNumericalInputValue(0) * getNumericalInputValue(1);
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
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
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == PlusOperator.class
				|| operator.getClass() == MinusOperator.class;
	}

}
