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

public class TestComponent implements ITestComponent, ICompareSinkListener{
	
	Logger logger = LoggerFactory.getLogger(TestComponent.class);


	static TestComponent instance = null;
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;
	
	public static String newline = System.getProperty("line.separator");
	ISession user = null;
	private boolean processingDone = false;
	private String errorText;

	BufferedWriter out = null;
	
	public void activate(ComponentContext context){
		instance = this;
	}
	
	public void bindExecutor(IExecutor executor){
		this.executor = (IServerExecutor)executor;
	}
	
	public void bindScriptParser(IOdysseusScriptParser scriptParser){
		this.parser = scriptParser;
	}
	
	@Override
	public void startTesting(String[] args){
		String dir = null;
		if (args.length == 3) {
			dir = args[0];
			user = UserManagement.getSessionmanagement().login(args[1], args[2].getBytes());
		}

		if (user == null){
			throw new RuntimeException("No valid user/password");
		}

		
		// Read queries from directory dir
		File f = new File(dir);
		logger.debug("Looking for files in " + f);
		File[] fileArray = f.listFiles();
		
		// Creating resultfile in dir
		try {
			out = new BufferedWriter(new FileWriter(dir+"/result"+System.currentTimeMillis()+".log"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
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
					} else {
						results.put(pre, file);
					}
				}
			}
		}

		logger.debug("Starting executor ...");

		try {
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}
		
		logger.debug("Processing queries ...");
		// TODO: Logging to file
		for (Entry<String, File> query : queries.entrySet()) {
			boolean success = true;
			processingDone = false;
			try {
				test(query.getKey(), query.getValue(),
						results.get(query.getKey()), parser);
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
				String text = "Query " + query.getKey() + " failed! "
						+ e.getMessage();
				logger.error(text);
				try {
					out.write(text+newline);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {

				if (success) {
					logger.debug("Query " + query.getKey()
							+ " successfull");
				}
			}
			// Warten bei Nexmark notwendig damit Daten wieder von vorne losgehen
			try {
				logger.debug("Waiting 30 seconds before next run");
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.flush();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			executor.stopExecution();
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void test(String key, File query, File result,
			IOdysseusScriptParser parser) throws OdysseusScriptException,
			IOException {
		String text ="Testing Query " + key + " from file " + query
				+ " with results from file " + result+ " --> "; 
		logger.debug(text);
		out.write(text);
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
	public synchronized void processingDone() {
		logger.debug("Query processing done");
		try {
			out.write(" ok "+newline);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processingDone = true;
		errorText = null;
		notifyAll();
	}

	@Override
	public synchronized void processingError(String line, String input) {
		String text ="Query processing created error " + line + " " + input;
		logger.error(text);
		try {
			out.write(text+newline);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processingDone = true;
		errorText = "Wrong Result input " + input + " was expecting " + line;
		notifyAll();
	}

}
