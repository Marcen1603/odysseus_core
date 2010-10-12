package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class PowerOperator extends AbstractBinaryOperator {

	@Override
	public int getPrecedence() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "^";
	}

	@Override
	public Double getValue() {
		return Math.pow((Double) getInputValue(0), (Double) getInputValue(1));
	}

}
