package de.uniol.inf.is.odysseus.parallelization.intraoperator.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.transformationhandler.AbstractParallelizationPreTransformationHandler;

public class IntraOperatorParallelizationPreTransformationHandler extends AbstractParallelizationPreTransformationHandler{

	private static final String HANDLER_NAME = "IntraOperatorParallelizationPreTransformationHandler";
	private final String TYPE = "INTRA_OPERATOR";
	

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getName() {
		return HANDLER_NAME;
	}
	
	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		// TODO Auto-generated method stub
		
	}

}
