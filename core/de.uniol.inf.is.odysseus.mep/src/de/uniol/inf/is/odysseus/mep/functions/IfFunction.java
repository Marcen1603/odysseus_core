package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class IfFunction extends AbstractFunction {

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "if";
	}

	@Override
	public Object getValue() {
		return (Double) getInputValue(0) == 0.0d ? getInputValue(2)
				: getInputValue(1);
	}

}
