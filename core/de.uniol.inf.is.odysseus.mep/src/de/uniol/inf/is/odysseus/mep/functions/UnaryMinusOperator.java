package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class UnaryMinusOperator extends AbstractUnaryOperator<Double> {

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "UnaryMinus";
	}

	@Override
	public Double getValue() {
		return -getNumericalInputValue(0);
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}
}
