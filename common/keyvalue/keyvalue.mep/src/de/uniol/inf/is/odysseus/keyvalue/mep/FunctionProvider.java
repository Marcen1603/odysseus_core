package de.uniol.inf.is.odysseus.keyvalue.mep;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> funcs = new ArrayList<IMepFunction<?>>();
		funcs.add(new KvSelectFunction());
		funcs.add(new KvPathFunction());
		funcs.add(new ToKeyValueFuntion());
		funcs.add(new KVGetElementFunction());
		funcs.add(new KVGetElementsFunction());
		return funcs;
	}

}
