package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class EqualsOperator extends AbstractBinaryOperator {

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public String getSymbol() {
		return "==";
	}

	@Override
	public Object getValue() {
		return getInputValue(0).equals(getInputValue(1)) ? 1.0d : 0.0d;
	}

}
