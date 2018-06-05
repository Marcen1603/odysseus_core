package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> funcs = new LinkedList<>();
		funcs.add(new MDAIndexFunction());
		funcs.add(new MDAIndicesFunction());
		funcs.add(new MDADimFunction());
		funcs.add(new MDAInitFunction());
		funcs.add(new MDADropFunction());
		funcs.add(new MDAAddDimFunction());
		funcs.add(new MDAAddDimWithIndexFunction());
		funcs.add(new MDARemoveDimFunction());
		funcs.add(new MDAExchangeDimFunction());
		return funcs;
	}

}
