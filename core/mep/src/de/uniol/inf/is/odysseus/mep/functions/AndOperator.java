package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class AndOperator extends AbstractBinaryOperator<Boolean> {

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public int getPrecedence() {
		return 13;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public String getSymbol() {
		return "&&";
	}

	@Override
	public Boolean getValue() {
		return (Boolean) getInputValue(0) && (Boolean) getInputValue(1);
	}

	@Override
	public Class<? extends Boolean> getType() {
		return Boolean.class;
	}

}
