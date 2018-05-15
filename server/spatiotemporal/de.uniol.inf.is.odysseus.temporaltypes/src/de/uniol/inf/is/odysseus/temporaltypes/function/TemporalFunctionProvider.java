package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class TemporalFunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
		functions.add(new TemporalizeIntegerFunction());
		functions.add(new TrimFunction());
		functions.add(new AtMinFunction());
		functions.add(new AtMaxFunction());
		return functions;
	}

}
