package de.uniol.inf.is.odysseus.test.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.ICompareSinkListener;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.TICompareSink;

public abstract class AbstractQueryTestComponent<T extends ITestContext> extends AbstractTestComponent<T> implements ICompareSinkListener {

	private static final long PROCESSING_WAIT_TIME = 1000;

	private StatusCode processingResult = null;

	protected StatusCode executeTestSet(TestSet set) {
		try {
			LOG.debug("starting component test...");

			// List<IQueryBuildSetting<?>> settings = new ArrayList<>();//
			LOG.debug("adding query...");
			Collection<Integer> ids = executor.addQuery(set.getQuery(), "OdysseusScript", session, "Standard", Context.empty());
			LOG.debug("adding " + ids.size() + " queries done. Preparing test...");
			for (Integer queryId : ids) {
				IPhysicalQuery physicalQuery = executor.getExecutionPlan().getQueryById(queryId);
				executor.stopQuery(queryId, session);
				List<IPhysicalOperator> roots = new ArrayList<>();
				for (IPhysicalOperator operator : physicalQuery.getRoots()) {
					// TODO: this assumes same output for all sinks -> maybe
					// there are multiple sinks with different outputs
					List<Pair<String, String>> expected = set.getExpectedOutput();
					TICompareSink sink = new TICompareSink(expected);
					sink.addListener(this);
					roots.add(sink);
					@SuppressWarnings("unchecked")
					ISource<Tuple<ITimeInterval>> oldSink = (ISource<Tuple<ITimeInterval>>) operator;
					oldSink.subscribeSink(sink, 0, 0, operator.getOutputSchema());
				}
				physicalQuery.setRoots(roots);
			}
			for (int id : ids) {
				LOG.debug("starting query " + id + "...");
				executor.startQuery(id, session);
			}
			processingResult = null;
			LOG.debug("query started, waiting until data is processed...");
			StatusCode result = waitProcessing();
			LOG.debug("processing done.");
			LOG.debug("result: " + result);
			LOG.debug("test done.");
		} catch (PlanManagementException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.removeAllQueries(session);
		}
		return this.processingResult;

	}

	public abstract List<TestSet> createTestSets(T context);

	private StatusCode waitProcessing() throws InterruptedException {
		synchronized (this) {
			while (processingResult == null) {
				this.wait(PROCESSING_WAIT_TIME);
			}
			return processingResult;
		}
	}

	@Override
	public void compareSinkProcessingDone(TICompareSink sink, boolean done, StatusCode result) {
		synchronized (this) {
			this.processingResult = result;
		}
	}

}
