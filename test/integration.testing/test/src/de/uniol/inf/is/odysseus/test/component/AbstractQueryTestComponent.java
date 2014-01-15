package de.uniol.inf.is.odysseus.test.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.ICompareSinkListener;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.TICompareSink;

public abstract class AbstractQueryTestComponent<T extends ITestContext, S extends QueryTestSet> extends AbstractTestComponent<T, S> implements ICompareSinkListener {

	private static final long PROCESSING_WAIT_TIME = 1000;

	private StatusCode processingResult = null;

	private boolean waitforprocessing = true;
	
	public AbstractQueryTestComponent(){
		this(true);
	}
	
	public AbstractQueryTestComponent(boolean waitForProcessing){
		this.waitforprocessing  = waitForProcessing;
	}

	@Override
	protected StatusCode executeTestSet(S set) {
		Collection<Integer> ids = new ArrayList<>();
		try {
			LOG.debug("starting component test...");
			LOG.debug("adding query...");
			ids = executor.addQuery(set.getQuery(), "OdysseusScript", session, "Standard", Context.empty());
		} catch (PlanManagementException e) {
			e.printStackTrace();
			return StatusCode.QUERY_NOT_INSTALLABLE;
		}
		try {
			LOG.debug("adding " + ids.size() + " queries done.");
			StatusCode result = prepareQueries(ids, set);
			if(result==StatusCode.OK && waitforprocessing){
				for (int id : ids) {
					LOG.debug("starting query " + id + "...");
					executor.startQuery(id, session);
				}
				processingResult = null;
				LOG.debug("query started, waiting until data is processed...");
				result = waitProcessing();
				LOG.debug("processing done.");
			}else{
				processingResult = result;
			}
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

	protected StatusCode prepareQueries(Collection<Integer> ids, S set) {		
		return StatusCode.OK;				
	}

	@Override
	public abstract List<S> createTestSets(T context);

	protected StatusCode waitProcessing() throws InterruptedException {
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
