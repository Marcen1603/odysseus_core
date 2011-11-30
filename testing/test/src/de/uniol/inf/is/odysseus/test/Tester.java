package de.uniol.inf.is.odysseus.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class Tester implements IApplication, ICompareSinkListener {

	Logger logger = LoggerFactory.getLogger(Tester.class);
	private boolean processingDone;
	private String errorText;

	public static String newline = System.getProperty("line.separator");
	User user = null;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(
				"application.args");
		
		BundleContext ctx = Activator.context;
		
		ServiceTracker t = new ServiceTracker(ctx,
				IExecutor.class.getName(), null);
		t.open();
		IExecutor executor = (IExecutor) t.waitForService(0);
		if (executor == null) {
			throw new Exception("cannot find executor service");
		}
		
		ServiceTracker p = new ServiceTracker(ctx,
				IOdysseusScriptParser.class.getName(), null);
		t.open();
		IOdysseusScriptParser scriptParser = (IOdysseusScriptParser) t.waitForService(0);
		if (scriptParser == null) {
			throw new Exception("cannot find script parser service");
		}
		

		String dir = null;
		if (args.length == 3) {
			dir = args[0];
			user = UserManagement.getInstance().login(args[1], args[2], false);
		}

		// Read queries from directory dir
		File f = new File(dir);
		logger.debug("Looking for files in " + f);
		File[] fileArray = f.listFiles();

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
					} else {
						results.put(pre, file);
					}
				}
			}
		}

		logger.debug("Processing queries ...");
		// TODO: Logging to file
		for (Entry<String, File> query : queries.entrySet()) {
			boolean success = true;
			try {
				test(query.getKey(), query.getValue(),
						results.get(query.getKey()), scriptParser);
				synchronized (this) {
					while (!processingDone) {
						this.wait(1000);
					}
				}
				// Stop all queries
				executor.stopExecution();
				for (IQuery q : executor.getQueries()) {
					executor.removeQuery(q.getID(), user);
				}
				if (errorText != null) {
					throw new RuntimeException(errorText);
				}

			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				logger.error("Query " + query.getKey() + " failed! "
						+ e.getMessage());
			} finally {
				if (success) {
					logger.debug("Query " + query.getKey() + " successfull");
				}
			}
		}

		return null;
	}

	private void test(String key, File query, File result, IOdysseusScriptParser parser)
			throws OdysseusScriptParseException, IOException {
		logger.debug("Testing Query " + key + " from file " + query
				+ " with results from file " + result);
		if (result == null) {
			throw new IllegalArgumentException("No result set found for query "
					+ key);
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
	public void stop() {

	}

	@Override
	public synchronized void processingDone() {
		logger.debug("Query processing done");
		processingDone = true;
		errorText = null;
		notifyAll();
	}

	@Override
	public synchronized void processingError(String line, String input) {
		logger.error("Query processing created error " + line + " " + input);
		processingDone = true;
		errorText = "Wrong Result input " + input + " was expecting " + line;
		notifyAll();
	}

}
