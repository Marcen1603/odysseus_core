package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.fusion.classification.function.ShapeClassify;
import de.uniol.inf.is.odysseus.fusion.function.SmoothPolygon;
import de.uniol.inf.is.odysseus.fusion.tracking.function.LPPrediction;
import de.uniol.inf.is.odysseus.fusion.tracking.function.LPTracking;

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
