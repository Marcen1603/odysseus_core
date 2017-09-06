/**********************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.benchmark.transformationhandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator.ObserverBenchmarkAO;

/**
 * Simple transformation handler, which integrates an ObserverBenchmarkAO at the
 * end of the current logical plan. Could be used with #TRANSFORM keyword in
 * Odysseus script
 *
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkPreTransformationHandler extends
		AbstractPreTransformationHandler {

	public static String NAME = "BenchmarkPreTransformation";

	/**
	 * returns the name of this preTransformationHandler
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * integrates an ObserverBenchmarkAO at the end of the current logical plan.
	 */
	@Override
	public void preTransform(IServerExecutor executor, ISession caller,
			ILogicalQuery query, QueryBuildConfiguration config,
			List<Pair<String, String>> handlerParameters, Context context) {
		ILogicalOperator topAO = query.getLogicalPlan().getRoot();

		// get topAO
		if (topAO instanceof TopAO) {
			CopyOnWriteArrayList<LogicalSubscription> sourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>(
					topAO.getSubscribedToSource());
			for (LogicalSubscription logicalSubscription : sourceSubscriptions) {
				// unsibscribe the topAO from all source subscriptions
				ILogicalOperator sourceOperator = logicalSubscription
						.getSource();
				topAO.unsubscribeFromSource(logicalSubscription);

				// create ObserverBenchmark operator and connect to all existing sources
				ObserverBenchmarkAO observerBenchmarkAO = new ObserverBenchmarkAO();
				sourceOperator.subscribeSink(observerBenchmarkAO, 0,
						logicalSubscription.getSourceOutPort(),
						logicalSubscription.getSchema());

				// reconnect existing topAO
				observerBenchmarkAO.subscribeSink(topAO,
						logicalSubscription.getSinkInPort(), 0,
						logicalSubscription.getSchema());
			}
		}
	}

}
