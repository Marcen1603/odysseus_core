package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.parallelization.physicaloperator.ObserverCounterPOHelper;
import de.uniol.inf.is.odysseus.parallelization.physicaloperator.ObserverCounterRegistry;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerExecution;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class BenchmarkExecutionThread extends Thread implements Observer{
	private static final int MAXIMUM_EXECUTION_TIME = 80000;
	private static Logger LOG = LoggerFactory
			.getLogger(BenchmarkThread.class);
	private String queryString;
	private BenchmarkerExecution benchmarkerExecution;
	private BenchmarkDataHandler data;
	private Collection<Integer> queryIds;
	private IExecutor executor;
	private UUID observerUUID;

	public BenchmarkExecutionThread(String queryString, UUID processUid,
			BenchmarkerExecution benchmarkerExecution) {
		this.queryString = queryString;
		this.data = BenchmarkDataHandler.getExistingInstance(processUid);
		this.benchmarkerExecution = benchmarkerExecution;
		this.observerUUID = UUID.randomUUID();
	}

	@Override
	public void run() {
		LOG.debug("Executing benchmark query");

		ObserverCounterRegistry registry = ObserverCounterRegistry.getInstance();
		registry.registerObserver(observerUUID, this);
		
		executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		queryIds = executor.addQuery(this.queryString,
				"OdysseusScript", OdysseusRCPPlugIn.getActiveSession(),
				ParserClientUtil.createRCPContext(data.getQueryFile()));
		for (Integer queryID : queryIds) {
			executor.startQuery(queryID, OdysseusRCPPlugIn.getActiveSession());
		}
		try {
			Thread.sleep(MAXIMUM_EXECUTION_TIME);
		} catch (InterruptedException e) {
			benchmarkerExecution.setExecutionTime(MAXIMUM_EXECUTION_TIME);
		}
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (observable instanceof ObserverCounterPOHelper){
			ObserverCounterPOHelper counterPOHelper = (ObserverCounterPOHelper) observable;
			ObserverCounterRegistry registry = ObserverCounterRegistry.getInstance();
			registry.unregisterObserver(counterPOHelper, observerUUID);
		}
		
		for (Integer queryId : queryIds) {
			executor.removeQuery(queryId, OdysseusRCPPlugIn.getActiveSession());
		}

		long executionTime = 0l;
		if (arg instanceof Long){
			executionTime = (Long) arg;
		} 
		benchmarkerExecution.setExecutionTime(executionTime);
	}
}
