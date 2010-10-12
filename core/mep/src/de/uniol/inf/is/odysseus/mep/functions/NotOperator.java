package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class NotOperator extends AbstractUnaryOperator<Boolean> {

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "!";
	}

	@Override
	public Boolean getValue() {
		return !((Boolean) getInputValue(0));
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}
}
