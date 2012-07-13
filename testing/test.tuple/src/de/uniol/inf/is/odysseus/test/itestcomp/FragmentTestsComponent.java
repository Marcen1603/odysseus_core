/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.test.itestcomp;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.TupleTestActivator;
import de.uniol.inf.is.odysseus.test.physicaloperator.TupleCompareSinkPO;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;
import de.uniol.inf.is.odysseus.test.tuple.ICompareSinkListener;


public class FragmentTestsComponent implements ITestComponent, ICompareSinkListener {

	private static Logger LOG = LoggerFactory.getLogger(FragmentTestsComponent.class);

	// OSGI injections
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;
	
	public void bindExecutor(IExecutor executor) {
		checkArgument(executor instanceof IServerExecutor, "Executor must be instance of " + IServerExecutor.class.getName() + " instead of " + executor.getClass().getName());
		this.executor = (IServerExecutor) executor;
	}

	public void bindScriptParser(IOdysseusScriptParser scriptParser) {
		parser = scriptParser;
	}
	
	private List<Long> alreadyTested = new ArrayList<Long>();	
	private List<Bundle> bundlesToTest = new ArrayList<Bundle>();
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private boolean isExecuting;
	private String errorText;

	private BufferedWriter out;
	private long startTime;
	
	private Writer logFile;
	
	public FragmentTestsComponent() {		
		try {
			File file = new File("ody.txt"); // TODO
			logFile = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {			
			e.printStackTrace();
		}
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
	private Map<String, List<File>> readQueries(File testsFolder) {
		HashMap<String, List<File>> result = new HashMap<String, List<File>>();

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

	// anstatt bunderltracker zuerst mit bundlelistener probieren
//	public void addTestsDirectory(File testsDir) {
//		// diese methode kann jederzeit aufgerufen werden wenn ein neues test-bundle gefunden wurde.
//		// der vorhandene ordner muss dann wieder ausgeführt werden
//		checkForTestsAndQueueThem(bundle);
//	}

	@Override
	public void setUp() {
		logIntoFile("SET UP " + this.getClass().getSimpleName() + " AT " + new Date().toString() + NEWLINE);
		// lese alle schon installierten bundles aus und führe deren tests durch
		for(Bundle b : TupleTestActivator.context.getBundles()){
			checkForTestsAndQueueThem(b);
		}		
		
		// TODO bundle listener
		// ... außerdem installiere bundleListener um nachträglich installierte bundles zu testen
//		TupleTestActivator.context.addBundleListener(new BundleListener() {
//			
//			@Override
//			public void bundleChanged(BundleEvent event) {
//				checkForTestsAndQueueThem(event.getBundle());
//			}
//		});		
	}
	
	private void checkForTestsAndQueueThem(Bundle bundle) {
		if (alreadyTested.contains(bundle.getBundleId())) {
			logIntoFile("ALREADY TESTED - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
			return;
		}

		// look for "tests" Folder
		URL fileUrl = bundle.getResource("tests");

		if(fileUrl == null){
			logIntoFile("NO TESTS FOUND - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
			return;
		}
		
		try {
			File testsDir = new File(FileLocator.toFileURL(fileUrl).getPath());

			// ... and add to queue for later testing if found
			if (testsDir != null) {
				logIntoFile("TESTS FOUND - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
				bundlesToTest.add(bundle);
				
//				if(!isExecuting)
//					startTesting(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File getTestsFolder(Bundle bundle) {
		// look for "tests" Folder
		URL fileUrl = bundle.getResource("tests");

		try {
			File testsDir = new File(FileLocator.toFileURL(fileUrl).getPath());

			return testsDir;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void executeTests2(int index){
		Bundle bundle = bundlesToTest.get(index);
		executeTests(bundle);
		
		if(bundlesToTest.size() - 1 > index++){
			executeTests2(index);
		}
	}
	
	private void executeTests(Bundle bundle){		
		if(alreadyTested.contains(bundle.getBundleId())){
			logIntoFile("BUNDLE ALREADY TESTED BEFORE - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
			return;			
		}
		
		logIntoFile("BUNDLE START TESTS - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
				
		// diese methode soll alle entahlten tests (param) ausführen
		File testFolder = getTestsFolder(bundle);
		Map<String, List<File>> testsQueries = readQueries(testFolder);
		
		checkNotNull(testFolder, "Testfolder not found");
		checkNotNull(executor, "Executor must be bound");
		checkNotNull(parser, "Parser must be bound");
		checkNotNull(UserManagement.getSessionmanagement(), "session management not set");
		
		LOG.debug("[TESTS] Processing tests for bundle {}", bundle.toString());
		
		ISession session = UserManagement.getSessionmanagement().login("System", "manager".getBytes());
		
		out = createWriter(new File("")); // TODO write into correct directory

		// try to start the executor
		try {
			LOG.debug("Starting executor");
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}

		LOG.debug("Processing " + testsQueries.size() + " queries ...");
		
		int errors = 0;
		
		for (Entry<String, List<File>> queries : testsQueries.entrySet()) {
			logIntoFile("CATEGORY START " + queries.getKey() + " WITH " + queries.getValue().size() + " TESTS" + NEWLINE);
			
			int success = 0;
			int fail = 0;
			
			for(File qry : queries.getValue()){
				final String queryName = qry.getName();;
				final File queryFile = qry;
				final File expectedResults = changeFileEnding(queryFile, "csv");
				
				try {
					logIntoFile("QUERY START - " + queryName + NEWLINE);
					test(queryName, queryFile, expectedResults, parser, session);
					
					executor.removeAllQueries(session);
					
					checkForErrors(errorText);
					LOG.debug("Query {} successfull", queryName);
					logIntoFile("QUERY END - " + queryName + " - SUCCESS" + NEWLINE);
					success++;
				} catch (OdysseusScriptException e) {
					LOG.error("Query {} failed! ", queryName, e);
					logIntoFile("QUERY END - " + queryName + " - FAILED" + NEWLINE);
					fail++;
					tryWrite(out, "Query " + queryName + " failed! " + NEWLINE + e.getMessage());
				} catch( IOException e ) {
	                LOG.error("Query {} failed! ", queryName, e);
	                logIntoFile("QUERY END - " + queryName + " - FAILED" + NEWLINE);
	                fail++;
	                tryWrite(out, "Query " + queryName + " failed! " + NEWLINE + e.getMessage());
				}
				
			}
			
			logIntoFile("CATEGORY END " + queries.getKey() + ": " + success + " SUCCESS " + fail + " FAILED" + NEWLINE);
			
			errors += fail;
		}
		
		tryClose(out);
		
		// try to stop the executor
		try {
			LOG.debug("Stopping executor");
			executor.stopExecution();
		} catch (PlanManagementException e) {
			LOG.error("Exception during stopping executor", e);
		}
		
		alreadyTested.add(bundle.getBundleId());
		
		LOG.debug("[TESTS] Finished tests for bundle {}", bundle.toString());
		
		logIntoFile("BUNDLE END - " + errors + " ERRORS IN THIS BUNDLE" + NEWLINE);
	}
	
	@Override
	public Object startTesting(String[] args) {
		logIntoFile("STARTED " + this.getClass().getSimpleName() + " AT " + new Date().toString()  + NEWLINE);
		
		if(isExecuting){
			logIntoFile("ALREADY EXECUTING" + NEWLINE);
			return "Already Executing";
		}
		
		isExecuting = true;
		
		if(bundlesToTest.size() > 0){
			executeTests2(0);
		}		 
		
		isExecuting = false;
		
		try {
			logFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Success";
	}

    private static BufferedWriter createWriter(File dir) {
    	String directory = dir.getAbsolutePath();
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

	private void test(String queryName, File query, File expcetedResults, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {		
		String text = "Testing Query " + queryName + " from file " + query;
		LOG.debug(text);
		tryWrite(out, text);
		
		startTime = System.nanoTime();
		
		String path = "#DEFINE PATH " + query.getAbsolutePath() + " \n";
		
		parser.parseAndExecute(path + getQueryString(query), user, new TupleCompareSinkPO(expcetedResults, this));
	}

	private static String getQueryString(File query) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(query));
		String line = null;
		StringBuffer queryString = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			queryString.append(line).append(NEWLINE);
		}
		reader.close();
		return queryString.toString();
	}
	
	private static File changeFileEnding(File file, String newEnding){
		File result = new File(file.getAbsolutePath().substring(0,
				file.getAbsolutePath().length() - 4)
				+ "." + newEnding);
		return result;
	}


	private void logIntoFile(String line) {
		try {
			logFile.write("[" + System.nanoTime() + "] " + line);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onQuerySuccessful() {
		logIntoFile("onQuerySuccessful");
	}

	@Override
	public void onQueryError(Object expected, Object real) {
		logIntoFile("onQueryError");
	}
}
