package de.uniol.inf.is.odysseus.mep.math;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.mep.commons.math.BinomialTestFunction;

public class MathFunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		final List<IFunction<?>> functions = new ArrayList<>();
		functions.add(new BinomialTestFunction());
		return functions;
	}

}
