package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SqrtValue extends AbstractFunction<Object>{

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "SqrtValue";
	}

	@Override
	public Object getValue() {
		return Math.sqrt((Double)getInputValue(0));
	}

	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
