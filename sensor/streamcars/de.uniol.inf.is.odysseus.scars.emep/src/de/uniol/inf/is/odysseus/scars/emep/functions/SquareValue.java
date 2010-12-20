package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SquareValue extends AbstractFunction<Object>{

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "SquareValue";
	}

	@Override
	public Object getValue() {
		return (Double)getInputValue(0)*(Double)getInputValue(0);
	}

	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
