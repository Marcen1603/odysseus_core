package de.uniol.inf.is.odysseus.multithreaded.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;


public abstract class AbstractParallelizationPreTransformationHandler implements
		IParallelizationPreTransformationHandler {
	
	@Override
	public PreTransformationHandlerParameter createHandlerParameter(
			int degreeOfParallelization) {
		PreTransformationHandlerParameter preTransformationHandlerParameter = new PreTransformationHandlerParameter();
		
		Pair<String, String> degreeParameter = new Pair<String, String>();
		degreeParameter
				.setE1(ParallelizationParameter.DEGREE_OF_PARALLELIZATION
						.name());
		degreeParameter.setE2(String.valueOf(degreeOfParallelization));

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(degreeParameter);

		preTransformationHandlerParameter.add(getName(), parameters);

		return preTransformationHandlerParameter;
	}

}
