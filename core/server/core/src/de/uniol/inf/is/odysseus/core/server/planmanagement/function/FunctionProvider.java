package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> funcs = new LinkedList<>();
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
		funcs.add(new QueryMaxSheddingFactorFunction());
		funcs.add(new GetSharedOpsCountFunction());
		funcs.add(new GetSourceCountFunction());
		return funcs;
	}

}
