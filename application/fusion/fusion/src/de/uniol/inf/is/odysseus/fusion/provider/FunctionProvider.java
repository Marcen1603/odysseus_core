package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.fusion.function.classification.ShapeClassify;
import de.uniol.inf.is.odysseus.fusion.function.extrapolation.SmoothPolygon;
import de.uniol.inf.is.odysseus.fusion.function.tracking.LPPrediction;
import de.uniol.inf.is.odysseus.fusion.function.tracking.LPTracking;

public class FunctionProvider implements IFunctionProvider{

	@Override
	public List<IFunction<?>> getFunctions() {
		
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		functions.add(new ShapeClassify());
		functions.add(new SmoothPolygon());
		functions.add(new LPPrediction());
		functions.add(new LPTracking());
		return functions;
	}

}
