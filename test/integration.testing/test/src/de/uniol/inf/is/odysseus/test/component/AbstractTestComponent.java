package de.uniol.inf.is.odysseus.test.component;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.test.ExecutorProvider;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;

public abstract class AbstractTestComponent<T extends ITestContext> implements ITestComponent<T>, IPlanModificationListener {

	private static final long PROCESSING_WAIT_TIME = 1000;
	protected static Logger LOG = LoggerFactory.getLogger(AbstractTestComponent.class);
	private IServerExecutor executor;
	private List<TestSet> testsets;
	private ISession session;
	private boolean processingDone = false;	

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType() == PlanModificationEventType.QUERY_STOP) {
			processingDone = true;
		}
	}

	@Override
	public void setupTest(T context) {
		executor = ExecutorProvider.getExeuctor();
		session = UserManagementProvider.getSessionmanagement().login(context.getUsername(), context.getPassword().getBytes(), UserManagementProvider.getDefaultTenant());
		executor.addPlanModificationListener(this);		
		testsets = createTestSets(context);				
	}

	private StatusCode executeTestSet(TestSet set) {
		try {
			processingDone = false;
			executor.addQuery(set.getQuery(), "OdysseusScript", session, "Standard", Context.empty());					
			waitProcessing();
			System.out.println("ok, we are done");
			// TODO: evaluate results
		} catch (PlanManagementException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.removeAllQueries(session);
		}
		
		return StatusCode.OK;

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

	private void waitProcessing() throws InterruptedException {
		synchronized (this) {
			while (!processingDone) {
				this.wait(PROCESSING_WAIT_TIME);
			}
		}
	}

	@Override
	public List<StatusCode> runTest(T context) {
		List<StatusCode> codes = new ArrayList<>();
		int i = 1;
		tryStartExecutor(executor);
		for (TestSet set : testsets) {
			System.out.println("Running sub test " + i + " of " + testsets.size()+"....");
			StatusCode code = executeTestSet(set);
			System.out.println("Running sub test " + i + " done, ended with code: "+code);
			codes.add(code);
			i++;
		}
		tryStopExecutor(executor);
		return codes;
	}

}
