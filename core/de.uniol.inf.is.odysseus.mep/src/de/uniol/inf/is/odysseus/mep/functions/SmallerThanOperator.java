package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

public class SmallerThanOperator extends AbstractBinaryOperator {

	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public String getSymbol() {
		return "<";
	}

	@Override
	public Object getValue() {
		return (Double) getInputValue(0) < (Double) getInputValue(1) ? 1.0d
				: 0.0d;
	}
}
