package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToStringFunction extends AbstractFunction<String> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "toString";
	}

	@Override
	public String getValue() {
		return Double.toString(getNumericalInputValue(0));
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

}
