package de.uniol.inf.is.odysseus.parallelization.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;

public interface IParallelizationPreTransformationHandler extends IPreTransformationHandler{

	PreTransformationHandlerParameter createHandlerParameter(
			int globalDegreeOfParallelization, int globalBufferSize, boolean allowCleanup);
	
	String getType();

}
