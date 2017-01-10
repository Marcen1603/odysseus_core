package de.uniol.inf.is.odysseus.core.server.store.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new LinkedList<>();
		funcs.add(new KvNamedStoreReadFunction());
		funcs.add(new KvNamedStoreWriteFunction());
		funcs.add(new KvNamedStoreRemoveFunction());
		return funcs;
	}

}
