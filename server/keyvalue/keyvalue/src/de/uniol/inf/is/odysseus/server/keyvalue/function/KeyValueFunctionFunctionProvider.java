package de.uniol.inf.is.odysseus.server.keyvalue.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class KeyValueFunctionFunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new ArrayList<>();
		funcs.add(new ToKeyValueFuntion());

		return funcs;
	}

}
