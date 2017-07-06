package de.uniol.inf.is.odysseus.incubation.graph.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.BestPostsDebsFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.CountNodesFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.ScoreUpdaterFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provider for map-function registration.
 * 
 * @author Kristian Bruns
 */
public class GraphFunctionProvider implements IFunctionProvider {
	
	public GraphFunctionProvider() {
	}
	
	@Override
	public List<IMepFunction<?>> getFunctions() {
		final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
		functions.add(new CountNodesFunction());
		functions.add(new BestPostsDebsFunction());
		functions.add(new ScoreUpdaterFunction());
		return functions;
	}

}
