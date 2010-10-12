package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class NotEqualsOperator extends AbstractBinaryOperator {

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public String getSymbol() {
		return "!=";
	}

	@Override
	public Object getValue() {
		return getInputValue(0).equals(getInputValue(1)) ? 0.0d : 1.0d;
	}

}
