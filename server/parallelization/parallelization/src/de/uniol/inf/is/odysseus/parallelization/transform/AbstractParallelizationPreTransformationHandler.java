package de.uniol.inf.is.odysseus.parallelization.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.parallelization.parameter.ParallelizationKeywordParameter;

public abstract class AbstractParallelizationPreTransformationHandler implements
		IParallelizationPreTransformationHandler {

	@Override
	public PreTransformationHandlerParameter createHandlerParameter(
			int globalDegreeOfParallelization, int globalBufferSize, boolean allowCleanup) {
		PreTransformationHandlerParameter preTransformationHandlerParameter = new PreTransformationHandlerParameter();

		// create parameter for global value (degree of parallelization)
		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		Pair<String, String> degreeParameter = new Pair<String, String>();
		degreeParameter
				.setE1(ParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION
						.name());
		degreeParameter.setE2(String.valueOf(globalDegreeOfParallelization));
		parameters.add(degreeParameter);

		// create parameter for global value (buffersize)
		Pair<String, String> buffersizeParameter = new Pair<String, String>();
		buffersizeParameter
				.setE1(ParallelizationKeywordParameter.BUFFERSIZE
						.name());
		buffersizeParameter.setE2(String.valueOf(globalBufferSize));
		parameters.add(buffersizeParameter);
		
		// create parameter for global value (allow cleanup)
		Pair<String, String> cleanupParameter = new Pair<String, String>();
		cleanupParameter
				.setE1(ParallelizationKeywordParameter.OPTIMIZATION
						.name());
		cleanupParameter.setE2(String.valueOf(allowCleanup));
		parameters.add(cleanupParameter);
		

		preTransformationHandlerParameter.add(getName(), parameters);

		return preTransformationHandlerParameter;
	}

}
