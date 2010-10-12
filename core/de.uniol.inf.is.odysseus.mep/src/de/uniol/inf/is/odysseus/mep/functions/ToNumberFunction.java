package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToNumberFunction extends AbstractFunction {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "toNumber";
	}

	@Override
	public Double getValue() {
		return Double.parseDouble((String) getInputValue(0));
	}

}
