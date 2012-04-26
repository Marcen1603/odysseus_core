package de.uniol.inf.is.odysseus.test;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;

/**
 * Test Component for Query-Tests with arbitrary Tuples
 * 
 * @author Kai Pancratz, Alexander Funk
 *
 */
public class TupleTestComponent implements ITestComponent {

	private static Logger LOG = LoggerFactory.getLogger(TupleTestComponent.class);
//
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;
//
	private static final String NEWLINE = System.getProperty("line.separator");
	private String errorText;
//
	private BufferedWriter out;
	private long startTime;

	public void activate(ComponentContext context) {
		
	}

	public void bindExecutor(IExecutor executor) {
		checkArgument(executor instanceof IServerExecutor, "Executor must be instance of " + IServerExecutor.class.getName() + " instead of " + executor.getClass().getName());
		this.executor = (IServerExecutor) executor;
	}

	public void bindScriptParser(IOdysseusScriptParser scriptParser) {
		parser = scriptParser;
	}

	@Override
	public Object startTesting(String[] args) {
		System.out.println("----------------------------------------------------------------------------> Start Testing");
		LOG.debug("----------------------------------------------------------------------------> Start Testing");
		
		ISessionManagement bla = UserManagement.getSessionmanagement();
		ISession session = bla.login("System", "manager".getBytes());

		Map<String, List<File>> querys = readQueries();
		
		tryStartExecutor(executor);
		
		out = createWriter(TupleTestActivator.getBundlePath().getAbsolutePath());
		
		LOG.debug("Processing queries ...");
		
		for(Entry<String, List<File>> groupQuery : querys.entrySet()){
			System.out.println("Starting tests from group: " + groupQuery.getKey());
			
			for(File qry : groupQuery.getValue()){
				try {
					test(qry, parser, session);
					
					executor.removeAllQueries(session);
					
					checkForErrors(errorText);
					LOG.debug("Query {} successfull", qry.getName());

				} catch (OdysseusScriptException e) {
					LOG.error("Query {} failed! ", qry.getName(), e);
					tryWrite(out, "Query " + qry.getName() + " failed! " + NEWLINE + e.getMessage());
				} catch( IOException e ) {
	                LOG.error("Query {} failed! ", qry.getName(), e);
	                tryWrite(out, "Query " + qry.getName() + " failed! " + NEWLINE + e.getMessage());
				}
			}
			
			System.out.println("Finished tests from group: " + groupQuery.getKey());
		}
		
		
		
//		out = createWriter(args[2]);

//		Map<String, File> queries = Maps.newHashMap();
//		Map<String, File> results = Maps.newHashMap();
//		ImmutableList<File> fileArray = determineQueryFiles(args[2]);

//		determineQueriesAndResults( fileArray, queries, results );
      
		

		
//		for (Entry<String, File> query : queries.entrySet()) {
//			
//			final String queryKey = query.getKey();
//			final File queryFile = query.getValue();
//			processingDone = false;
//			
//			try {
//				test(queryKey, queryFile, results.get(queryKey), parser, session);
//				waitProcessing();
//				
//				executor.removeAllQueries(session);
//				
//				checkForErrors(errorText);
//				LOG.debug("Query {} successfull", queryKey);
//
//			} catch (OdysseusScriptException e) {
//				LOG.error("Query {} failed! ", queryKey, e);
//				tryWrite(out, "Query " + queryKey + " failed! " + NEWLINE + e.getMessage());
//			} catch( IOException e ) {
//                LOG.error("Query {} failed! ", queryKey, e);
//                tryWrite(out, "Query " + queryKey + " failed! " + NEWLINE + e.getMessage());
//			}
//		}
		tryClose(out);
		tryStopExecutor(executor);
		LOG.error("Testing finished");
		
		return "Success";
	}

	/**
	 * Diese Methode liest den tests/-Ordner der Komponente aus
	 * und speichert die Queries, welche eine passende Result-csv haben, 
	 * als gruppierte Liste.
	 * Unterordner des Gruppenordners werden nicht durchsucht und können
	 * als Datenspeicher benutzt werden.
	 * 
	 * tests/{GRUPPE}/q1.qry
	 * tests/{GRUPPE}/q1.csv
	 * 
	 * @return
	 */
	private Map<String, List<File>> readQueries() {
		HashMap<String, List<File>> result = new HashMap<String, List<File>>();

		File testsFolder = TupleTestActivator.getBundlePath();

		for (File folder : testsFolder.listFiles()) {
			if (!folder.isDirectory())
				continue;

			List<File> qrys = new ArrayList<File>();

			for (File file : folder.listFiles()) {
				if (file.isDirectory())
					continue;

				File csv = new File(file.getAbsolutePath().substring(0,
						file.getAbsolutePath().length() - 4)
						+ ".csv");
				if (file.getName().endsWith(".qry") && csv.exists())
					qrys.add(file);
			}

			result.put(folder.getName(), qrys);
		}

		return result;
	}

    private static BufferedWriter createWriter(String directory) {
        checkNotNull(directory, "directory must not be null");
        
        // Creating resultfile in dir
		String filename = directory + "/result" + System.currentTimeMillis() + ".log";
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			LOG.debug("Created result file " + filename);
			return out;
		} catch (IOException e) {
			LOG.error("Error creating file " + filename, e);
			throw new RuntimeException("Error during creating filename " + filename, e);
		}
    }
	
	private static void checkForErrors(String errorText ) {
		if( errorText != null ) {
			throw new RuntimeException(errorText);
		}
	}

	private static void tryStartExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Starting executor");
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}
	}

	private static void tryStopExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Stopping executor");
			executor.stopExecution();
		} catch (PlanManagementException e) {
			LOG.error("Exception during stopping executor", e);
		}
	}

	private static void tryClose(BufferedWriter out) {
		try {
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void tryWrite(BufferedWriter out, String text) {
		try {
			out.write(text);
		} catch( IOException ignored ) {
		}
	}

	private void test(File query, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {				
		String text = "Testing Query " + query.getName() + " from file " + query;
		LOG.debug(text);
		tryWrite(out, text);
		
		startTime = System.nanoTime();
//		parser.execute(getQueryString(query), user, null);
//		parser.parseScript(getQueryString(query), user);
		
		parser.parseAndExecute(getQueryString(query), user, null);
	}

	private static String getQueryString(File query) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(query));
		String line = null;
		StringBuffer queryString = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			queryString.append(line).append(NEWLINE);
		}
		return queryString.toString();
	}

//	@Override
//	public synchronized void processingDone() {
//	    long elapsedTimeMillis = ( System.nanoTime() - startTime ) / 1000000;
//	    
//		LOG.debug("Query processing done. Duration = " + elapsedTimeMillis + " ms");
//		tryWrite(out, " ok duration=" + elapsedTimeMillis + NEWLINE);
//
//		processingDone = true;
//		errorText = null;
//		
//		notifyAll();
//	}
//
//	@Override
//	public synchronized void processingError(String line, String input) {
//		String text = "Query processing created error " + line + " " + input;
//		LOG.error(text);
//		tryWrite(out, text);
//
//		processingDone = true;
//		errorText = "Wrong Result input '" + input + "'. Expected: '" + line + "'";
//		notifyAll();
//	}
//
//	@Override
//	public String toString() {
//		return NEXMARK_TESTS_NAME;
//	}

}
