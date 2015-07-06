package de.uniol.inf.is.odysseus.parallelization.benchmark.transformationhandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator.ObserverBenchmarkAO;

public class BenchmarkPreTransformationHandler implements
		IPreTransformationHandler {

	@Override
	public String getName() {
		return "BenchmarkPreTransformation";
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		ILogicalOperator topAO = query.getLogicalPlan();

		if (topAO instanceof TopAO) {
			CopyOnWriteArrayList<LogicalSubscription> sourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>(
					topAO.getSubscribedToSource());
			for (LogicalSubscription logicalSubscription : sourceSubscriptions) {
				ILogicalOperator sourceOperator = logicalSubscription
						.getTarget();
				topAO.unsubscribeFromSource(logicalSubscription);

				ObserverBenchmarkAO observerBenchmarkAO = new ObserverBenchmarkAO();

				sourceOperator.subscribeSink(observerBenchmarkAO, 0,
						logicalSubscription.getSourceOutPort(),
						logicalSubscription.getSchema());

				observerBenchmarkAO.subscribeSink(topAO,
						logicalSubscription.getSinkInPort(), 0,
						logicalSubscription.getSchema());
			}
		}
	}

}
