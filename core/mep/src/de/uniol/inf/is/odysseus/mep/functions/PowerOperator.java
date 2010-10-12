package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class PowerOperator extends AbstractBinaryOperator<Double> {

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
		return Math.pow(getNumericalInputValue(0), getNumericalInputValue(1));
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}
