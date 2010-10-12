package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class GreaterEqualsOperator extends AbstractBinaryOperator<Boolean> {
	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public String getSymbol() {
		return ">=";
	}

	@Override
	public Boolean getValue() {
		return getNumericalInputValue(0) >= getNumericalInputValue(1);
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}
}
