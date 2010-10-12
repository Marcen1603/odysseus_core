package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class UnaryMinusOperator extends AbstractUnaryOperator {

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
		return -(Double)getInputValue(0); 
	}

}
