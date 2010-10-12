package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class Now extends AbstractFunction<Long> {

	@Override
	public String getSymbol() {
		return "Now";
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public Long getValue() {
		return System.currentTimeMillis();
	}

	@Override
	public Class<? extends Long> getType() {
		return Long.class;
	}

}
