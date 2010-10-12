package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class NotOperator extends AbstractUnaryOperator {

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "!";
	}

	@Override
	public Double getValue() {
		return (Double)getInputValue(0) == 0.0 ? 1.0 : 0.0;
	}

}
