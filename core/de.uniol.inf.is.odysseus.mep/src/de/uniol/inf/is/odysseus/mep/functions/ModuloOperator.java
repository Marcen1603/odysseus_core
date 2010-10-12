package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class ModuloOperator extends AbstractBinaryOperator {

	@Override
	public int getPrecedence() {
		return 5;
	}

	@Override
	public String getSymbol() {
		return "%";
	}

	@Override
	public Double getValue() {
		return (Double) getInputValue(0) % (Double) getInputValue(1);
	}

}
