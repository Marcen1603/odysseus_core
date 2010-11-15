package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class IfFunction extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "eif";
	}

	@Override
	public Object getValue() {
		return getInputValue(0) ? getInputValue(1) : getInputValue(2);
	}

	@Override
	public Class<?> getType() {
		// if then and else arguments have the same type, we are sure to return
		// a value of that type
		if (getArguments()[1].getType() == getArguments()[2].getType()) {
			return getArguments()[1].getType();
		}
		// otherwise we make no guarantees
		return Object.class;
	}

}
