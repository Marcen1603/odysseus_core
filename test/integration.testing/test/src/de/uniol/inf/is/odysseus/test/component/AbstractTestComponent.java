package de.uniol.inf.is.odysseus.test.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.test.ExecutorProvider;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.ICompareSinkListener;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.TICompareSink;

public abstract class AbstractTestComponent<T extends ITestContext> implements ITestComponent<T>, ICompareSinkListener {

	private static final long PROCESSING_WAIT_TIME = 1000;
	protected static Logger LOG = LoggerFactory.getLogger(AbstractTestComponent.class);
	private IServerExecutor executor;
	private List<TestSet> testsets;
	private ISession session;
//	private boolean processingDone = false;
	private StatusCode processingResult = null;

	@Override
	public void setupTest(T context) {
		executor = ExecutorProvider.getExeuctor();
		session = UserManagementProvider.getSessionmanagement().login(context.getUsername(), context.getPassword().getBytes(), UserManagementProvider.getDefaultTenant());
		testsets = createTestSets(context);
	}

	private StatusCode executeTestSet(TestSet set) {
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

	public abstract T createTestContext();

	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStartExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Starting executor");
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}
	}

	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStopExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Stopping executor");
			executor.stopExecution();
		} catch (PlanManagementException e) {
			LOG.error("Exception during stopping executor", e);
		}
	}

	private StatusCode waitProcessing() throws InterruptedException {
		synchronized (this) {
			while (processingResult==null) {
				this.wait(PROCESSING_WAIT_TIME);				
			}
			return processingResult;
		}
	}

	@Override
	public List<StatusCode> runTest(T context) {
		List<StatusCode> codes = new ArrayList<>();
		int i = 1;
		tryStartExecutor(executor);
		for (TestSet set : testsets) {
			System.out.println("Running sub test " + i + " of " + testsets.size() + "....");
			StatusCode code = executeTestSet(set);
			System.out.println("Running sub test " + i + " done, ended with code: " + code);
			codes.add(code);
			i++;
		}
		tryStopExecutor(executor);
		return codes;
	}

	@Override
	public void compareSinkProcessingDone(TICompareSink sink, boolean done, StatusCode result) {
		synchronized (this) {
			this.processingResult = result;			
		}
	}
	
	@Override
	public String getName() {
		return getClass().getSimpleName();		
	}
}
