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
			int globalDegreeOfParallelization, int globalBufferSize) {
		PreTransformationHandlerParameter preTransformationHandlerParameter = new PreTransformationHandlerParameter();

		Pair<String, String> degreeParameter = new Pair<String, String>();
		degreeParameter
				.setE1(ParallelizationParameter.GLOBAL_DEGREE_OF_PARALLELIZATION
						.name());
		degreeParameter.setE2(String.valueOf(globalDegreeOfParallelization));

		Pair<String, String> buffersizeParameter = new Pair<String, String>();
		buffersizeParameter
				.setE1(ParallelizationParameter.GLOBAL_BUFFERSIZE
						.name());
		buffersizeParameter.setE2(String.valueOf(globalBufferSize));

		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(degreeParameter);
		parameters.add(buffersizeParameter);

		preTransformationHandlerParameter.add(getName(), parameters);

		return preTransformationHandlerParameter;
	}

}
