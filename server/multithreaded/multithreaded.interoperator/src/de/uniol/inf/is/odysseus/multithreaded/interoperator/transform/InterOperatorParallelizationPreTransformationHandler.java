package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.keyword.ParallelizationParameter;
import de.uniol.inf.is.odysseus.multithreaded.transform.AbstractParallelizationPreTransformationHandler;

public class InterOperatorParallelizationPreTransformationHandler extends
		AbstractParallelizationPreTransformationHandler {

	private static final String HANDLER_NAME = "InterOperatorParallelizationPreTransformationHandler";
	private final String TYPE = "INTER_OPERATOR";
	private final int MIN_PARAMETER_COUNT = 1;

	@SuppressWarnings("unused")
	private int degreeOfParallelization = 0;

	@Override
	public String getName() {
		return HANDLER_NAME;
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		if (handlerParameters.size() < MIN_PARAMETER_COUNT) {
			throw new IllegalArgumentException();
		} else {
			for (Pair<String, String> pair : handlerParameters) {
				ParallelizationParameter parameter = ParallelizationParameter
						.getParameterByName(pair.getE1());
				switch (parameter) {
				case DEGREE_OF_PARALLELIZATION:
					try {
						degreeOfParallelization = Integer
								.parseInt(pair.getE2());
					} catch (Exception e) {
						throw new IllegalAccessError();
					}
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
