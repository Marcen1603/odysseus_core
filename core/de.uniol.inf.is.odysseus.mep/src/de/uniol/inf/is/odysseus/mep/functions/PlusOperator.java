package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;


public class PlusOperator extends AbstractBinaryOperator<Double> {

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	@Override
	public Double getValue() {
		return getNumericalInputValue(0) + getNumericalInputValue(1);
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}
