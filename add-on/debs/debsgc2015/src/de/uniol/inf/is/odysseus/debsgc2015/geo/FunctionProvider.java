package de.uniol.inf.is.odysseus.debsgc2015.geo;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> funcs = new LinkedList<>();
		funcs.add(new LatLongDistance());
		funcs.add(new LatLongToUtm());
		return funcs;
	}

}
