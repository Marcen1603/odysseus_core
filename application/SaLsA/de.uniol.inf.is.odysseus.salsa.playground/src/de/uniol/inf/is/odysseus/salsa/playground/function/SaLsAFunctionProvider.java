package de.uniol.inf.is.odysseus.salsa.playground.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class SaLsAFunctionProvider implements IFunctionProvider{

	@Override
	public List<IFunction<?>> getFunctions() {
		
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		//functions.add(new SpatialContains());
		
		return functions;
	}

}
