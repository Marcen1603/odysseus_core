package de.uniol.inf.is.odysseus.keyvalue.mep;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new ArrayList<IFunction<?>>();
		funcs.add(new KvSelectFunction());
		funcs.add(new KvPathFunction());
		funcs.add(new ToKeyValueFuntion());
		funcs.add(new KVGetElementFunction());
		funcs.add(new KVGetElementsFunction());
		return funcs;
	}

}
