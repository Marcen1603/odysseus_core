package de.uniol.inf.is.odysseus.debs2013.mep.functions;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class GrandChallenge2013FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		functions.add(new DebsIntensityCalc());
		functions.add(new DebsIntensityCalc_Numeric());
		functions.add(new DebsIntensityCalc2());


		return functions;
	}

}
