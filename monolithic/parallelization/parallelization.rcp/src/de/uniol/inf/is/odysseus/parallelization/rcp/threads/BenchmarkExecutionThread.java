package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerExecution;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class BenchmarkExecutionThread extends Thread implements Observer{
	private static Logger LOG = LoggerFactory
			.getLogger(BenchmarkThread.class);
	private String queryString;
	private BenchmarkerExecution benchmarkerExecution;
	private BenchmarkDataHandler data;
	private Collection<Integer> queryIds;
	private long startTimeMillis;
	private IExecutor executor;

	public BenchmarkExecutionThread(String queryString, UUID processUid,
			BenchmarkerExecution benchmarkerExecution) {
		this.queryString = queryString;
		this.data = BenchmarkDataHandler.getExistingInstance(processUid);
		this.benchmarkerExecution = benchmarkerExecution;
	}

	@Override
	public void run() {
		LOG.debug("Executing benchmark query");

		startTimeMillis = System.currentTimeMillis();

		executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		queryIds = executor.addQuery(this.queryString,
				"OdysseusScript", OdysseusRCPPlugIn.getActiveSession(),
				ParserClientUtil.createRCPContext(data.getQueryFile()));
		for (Integer queryID : queryIds) {
			executor.startQuery(queryID, OdysseusRCPPlugIn.getActiveSession());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		for (Integer queryId : queryIds) {
			executor.removeQuery(queryId, OdysseusRCPPlugIn.getActiveSession());
		}

		// end time
		long endTimeMillis = System.currentTimeMillis();
		
		// calculate execution time
		long executionTime = endTimeMillis - startTimeMillis;
		benchmarkerExecution.setExecutionTime(executionTime);
	}
}
