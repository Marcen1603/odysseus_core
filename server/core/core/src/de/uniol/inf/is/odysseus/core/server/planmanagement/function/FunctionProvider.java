package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new LinkedList<>();
		funcs.add(new RetrieveQueryIDsFunction());
		funcs.add(new FilterQueryIDsFunction());		
		funcs.add(new QueryPriorityFunction());
		funcs.add(new IsACQueryFunction());
		funcs.add(new QueryBasePriorityFunction());
		funcs.add(new QueryStateFunction());
		funcs.add(new QueryNameFunction());
		funcs.add(new QueryStartTSFunction());
		funcs.add(new QueryLastStateChangeTSFunction());
		funcs.add(new QuerySheddingFactorFunction());
		
		return funcs;
	}

}
