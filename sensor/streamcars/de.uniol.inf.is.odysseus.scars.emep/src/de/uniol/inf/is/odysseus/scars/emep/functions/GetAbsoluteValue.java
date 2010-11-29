package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class GetAbsoluteValue extends AbstractFunction<Object>{

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "AbsValue";
	}

	@Override
	public Object getValue() {
		if((Double)getInputValue(0) < 1) {
			return ((Double)getInputValue(0)*(-1));
		} else {
			return getInputValue(0);
		}
	}

	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
