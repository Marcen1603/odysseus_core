package de.uniol.inf.is.odysseus.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;

public class TestComponent implements ITestComponent, ICompareSinkListener {

	private static Logger LOG = LoggerFactory.getLogger(TestComponent.class);

	private IServerExecutor executor;
	private IOdysseusScriptParser parser;

	public static String newline = System.getProperty("line.separator");
	ISession user = null;
	private boolean processingDone = false;
	private String errorText;

	BufferedWriter out = null;

	public void activate(ComponentContext context) {
	}

	public void bindExecutor(IExecutor executor) {
		LOG.info("Executor bound");
		this.executor = (IServerExecutor) executor;
	}

	public void bindScriptParser(IOdysseusScriptParser scriptParser) {
		LOG.info("ScriptParser bound");
		this.parser = scriptParser;
	}

	@Override
	public Object startTesting() {
		String dir = "nexmark";
		user = UserManagement.getSessionmanagement().login("System", "manager".getBytes());

		// Read queries from directory dir
		File f = new File(dir);
		LOG.debug("Looking for files in " + f);
		File[] fileArray = f.listFiles();

		// Creating resultfile in dir
		try {
			out = new BufferedWriter(new FileWriter(dir + "/result" + System.currentTimeMillis() + ".log"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		Map<String, File> queries = new HashMap<String, File>();
		Map<String, File> results = new HashMap<String, File>();

		if (fileArray != null) {
			for (File file : fileArray) {
				if (file.isFile()) {
					String name = file.getName();
					int point = name.lastIndexOf(".");
					String pre = name.substring(0, point);
					String post = name.substring(point + 1);
					if (post.equalsIgnoreCase("qry")) {
						queries.put(pre, file);
					} else if( post.equalsIgnoreCase("csv")){
						results.put(pre, file);
					}
				}
			}
		}

		LOG.debug("Starting executor ...");

		try {
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}

		LOG.debug("Processing queries ...");
		// TODO: Logging to file
		for (Entry<String, File> query : queries.entrySet()) {
			boolean success = true;
			processingDone = false;
			try {
				test(query.getKey(), query.getValue(), results.get(query.getKey()), parser);
				synchronized (this) {
					while (!processingDone) {
						this.wait(1000);
					}
				}
				// Stop all queries
				executor.removeAllQueries(user);
				if (errorText != null) {
					throw new RuntimeException(errorText);
				}

			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				String text = "Query " + query.getKey() + " failed! " + e.getMessage();
				LOG.error(text);
				try {
					out.write(text + newline);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {

				if (success) {
					LOG.debug("Query " + query.getKey() + " successfull");
				}
			}
		}
		try {
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			executor.stopExecution();
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
		
		return "Success";
	}

	private void test(String key, File query, File result, IOdysseusScriptParser parser) throws OdysseusScriptException, IOException {
		String text = "Testing Query " + key + " from file " + query + " with results from file " + result + " --> ";
		LOG.debug(text);
		out.write(text);
		if (result == null) {
			throw new IllegalArgumentException("No result set found for query " + key);
		}

		ICompareSink compareSink = new SimpleCompareSink(result, this);

		BufferedReader reader = new BufferedReader(new FileReader(query));
		String line = null;
		StringBuffer queryString = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			queryString.append(line).append(newline);
		}

		parser.parseAndExecute(queryString.toString(), user, compareSink);
	}

	@Override
	public synchronized void processingDone() {
		LOG.debug("Query processing done");
		try {
			out.write(" ok " + newline);
		} catch (IOException e) {
			e.printStackTrace();
		}
		processingDone = true;
		errorText = null;
		notifyAll();
	}

	@Override
	public synchronized void processingError(String line, String input) {
		String text = "Query processing created error " + line + " " + input;
		LOG.error(text);
		try {
			out.write(text + newline);
		} catch (IOException e) {
			e.printStackTrace();
		}
		processingDone = true;
		errorText = "Wrong Result input '" + input + "'. Expected: '" + line + "'";
		notifyAll();
	}

	@Override
	public String toString() {
		return "Nexmark Tests";
	}

}
