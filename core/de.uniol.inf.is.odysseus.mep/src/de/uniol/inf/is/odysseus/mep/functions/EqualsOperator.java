package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class EqualsOperator extends AbstractBinaryOperator<Boolean> {

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public String getSymbol() {
		return "==";
	}

	@Override
	public Boolean getValue() {
		return getInputValue(0).equals(getInputValue(1));
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

}
