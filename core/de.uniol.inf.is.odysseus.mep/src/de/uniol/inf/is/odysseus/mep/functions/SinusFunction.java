package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SinusFunction extends AbstractFunction {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "sin";
	}

	@Override
	public Double getValue() {
		return Math.sin((Double) getInputValue(0));
	}

}
