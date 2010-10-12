package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;


public class PlusOperator extends AbstractBinaryOperator {

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
		return (Double)getInputValue(0) + (Double)getInputValue(1);
	}

}
