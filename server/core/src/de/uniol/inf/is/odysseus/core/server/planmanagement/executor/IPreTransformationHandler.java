package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IPreTransformationHandler {
	public String getName();

	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context);
}
