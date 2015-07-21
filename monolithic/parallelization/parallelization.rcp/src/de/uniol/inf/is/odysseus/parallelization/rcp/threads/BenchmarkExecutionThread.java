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
package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.Collection;
import java.util.Observable;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.BenchmarkObserverRegistry;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.BenchmarkPOObservable;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.CountElementsBenchmarkEvaluation;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.IBenchmarkEvaluation;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.IBenchmarkObserver;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.IBenchmarkerExecution;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class BenchmarkExecutionThread extends Thread implements
		IBenchmarkObserver {
	private static Logger LOG = LoggerFactory.getLogger(BenchmarkMainExecutionThread.class);

	private String queryString;
	private IBenchmarkerExecution benchmarkerExecution;
	private BenchmarkDataHandler data;
	private Collection<Integer> queryIds;
	private IExecutor executor;
	private UUID observerUUID;
	private long maximumExecutionTime;

	public BenchmarkExecutionThread(String queryString, UUID processUid,
			IBenchmarkerExecution benchmarkerExecution) {
		this.queryString = queryString;
		this.data = BenchmarkDataHandler.getExistingInstance(processUid);
		this.benchmarkerExecution = benchmarkerExecution;
		this.observerUUID = UUID.randomUUID();
		this.maximumExecutionTime = data.getConfiguration().getMaximumExecutionTime();
	}

	@Override
	public void run() {
		LOG.debug("Executing benchmark query");

		BenchmarkObserverRegistry registry = BenchmarkObserverRegistry
				.getInstance();
		registry.registerObserver(observerUUID, this);

		executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		queryIds = executor.addQuery(this.queryString, "OdysseusScript",
				OdysseusRCPPlugIn.getActiveSession(),
				ParserClientUtil.createRCPContext(data.getQueryFile()));
		for (Integer queryID : queryIds) {
			executor.startQuery(queryID, OdysseusRCPPlugIn.getActiveSession());
		}
		try {
			Thread.sleep(maximumExecutionTime);
			benchmarkerExecution.addExecutionTime(maximumExecutionTime);
		} catch (InterruptedException e) {
		} finally {
			for (Integer queryId : queryIds) {
				executor.removeQuery(queryId,
						OdysseusRCPPlugIn.getActiveSession());
			}
			registry.unregisterObserver(null, observerUUID);
		}
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable instanceof BenchmarkPOObservable) {
			BenchmarkPOObservable<?> counterPOHelper = (BenchmarkPOObservable<?>) observable;
			BenchmarkObserverRegistry registry = BenchmarkObserverRegistry
					.getInstance();
			registry.unregisterObserver(counterPOHelper, observerUUID);
		}
		long executionTime = 0l;
		if (arg instanceof Long) {
			executionTime = (Long) arg;
		}
		benchmarkerExecution.addExecutionTime(executionTime);
		this.interrupt();

	}

	@Override
	public IBenchmarkEvaluation getBenchmarkEvaluation() {
		return new CountElementsBenchmarkEvaluation(
				this, data.getConfiguration().getNumberOfElements());
	}
}
