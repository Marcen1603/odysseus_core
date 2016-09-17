package de.uniol.inf.is.odysseus.incubation.graph.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.BestPostsDebsFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.CountNodesFunction;
import de.uniol.inf.is.odysseus.incubation.graph.functions.ScoreUpdaterFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class GraphFunctionProvider implements IFunctionProvider {
	
	public GraphFunctionProvider() {
	}
	
	@Override
	public List<IFunction<?>> getFunctions() {
		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		functions.add(new CountNodesFunction());
		functions.add(new BestPostsDebsFunction());
		functions.add(new ScoreUpdaterFunction());
		return functions;
	}

}
