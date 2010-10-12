package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToNumberFunction extends AbstractFunction<Double> {

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

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}
