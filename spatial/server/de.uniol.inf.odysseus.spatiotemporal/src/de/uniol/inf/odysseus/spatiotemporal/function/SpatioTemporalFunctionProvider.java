package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class SpatioTemporalFunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
		functions.add(new FromTemporalGeoJson());
		functions.add(new TrajectoryFunction());
		return functions;
	}

}
