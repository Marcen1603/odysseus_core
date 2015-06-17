package de.uniol.inf.is.odysseus.multithreaded.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;

public interface IParallelizationPreTransformationHandler extends IPreTransformationHandler{

	PreTransformationHandlerParameter createHandlerParameter(
			int globalDegreeOfParallelization, int globalBufferSize);
	
	String getType();

}
