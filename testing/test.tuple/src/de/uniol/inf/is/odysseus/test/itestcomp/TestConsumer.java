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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.physicaloperator.TupleCompareSinkPO;
import de.uniol.inf.is.odysseus.test.tuple.ICompareSinkListener;

/**
 * This class is normally run in a new thread and is using a BlockingQueue 
 * to consume Bundles. It checks for a folder "tests" and i found will 
 * execute all tests inside.
 * 
 * @author Alexander Funk
 *
 */
public class TestConsumer implements Runnable, ICompareSinkListener {
	private static Logger LOG = LoggerFactory.getLogger(TestConsumer.class);
	
	private final BlockingQueue<Bundle> queue;
	
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	public TestConsumer(BlockingQueue<Bundle> q, IServerExecutor e, IOdysseusScriptParser p) {
		queue = q;
		executor = e;
		parser = p;
	}
	
	public void run(){
		try {
			LOG.debug("Started TestConsumer in new Thread for Test Execution.");
			while(true){
				consume(queue.take());
			}
		} catch (InterruptedException e) {
			// handle exception
		}
	}
	
	void consume(Bundle bundle){
		// tests ausführen		
		logIntoFile("BUNDLE START TESTS - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
				
		File testFolder = getTestsFolder(bundle);
		Map<String, List<File>> testsQueries = readQueries(testFolder);
		
		checkNotNull(testFolder, "Testfolder not found");
		checkNotNull(executor, "Executor must be bound");
		checkNotNull(parser, "Parser must be bound");
		checkNotNull(UserManagement.getSessionmanagement(), "session management not set");
				
		ISession session = UserManagement.getSessionmanagement().login("System", "manager".getBytes());
		
		// try to start the executor
		if (!executor.isRunning()){
			try {		
				executor.startExecution();
			} catch (PlanManagementException e1) {
				throw new RuntimeException(e1);
			}
		}
		
		int errors = 0;
		
		for (Entry<String, List<File>> queries : testsQueries.entrySet()) {
			logIntoFile(" CATEGORY START " + queries.getKey() + " WITH " + queries.getValue().size() + " TESTS" + NEWLINE);
			
			int success = 0;
			int fail = 0;
			
			for(File qry : queries.getValue()){
				final String queryName = qry.getName();;
				final File queryFile = qry;
				final File expectedResults = changeFileEnding(queryFile, "csv");
				
				try {
					logIntoFile("  QUERY START - " + queryName + NEWLINE);
					test(queryName, queryFile, expectedResults, parser, session);
					
					executor.removeAllQueries(session);

					logIntoFile("  QUERY END - " + queryName + " - SUCCESS" + NEWLINE);
					success++;
				} catch (OdysseusScriptException e) {
					logIntoFile("  QUERY END - " + queryName + " - FAILED" + NEWLINE);
					fail++;
				} catch( IOException e ) {	                
	                logIntoFile("  QUERY END - " + queryName + " - FAILED" + NEWLINE);
	                fail++;
	            }
				
			}
			
			logIntoFile(" CATEGORY END " + queries.getKey() + ": " + success + " SUCCESS " + fail + " FAILED" + NEWLINE);
			
			errors += fail;
		}

		logIntoFile("BUNDLE END - " + errors + " ERRORS IN THIS BUNDLE" + NEWLINE);
	}
	


	private void test(String queryName, File query, File expcetedResults, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {
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

	private void logIntoFile(String string) {
		FragmentTestsComponent.logIntoFile(string);
	}

	@Override
	public void onQuerySuccessful() {
		logIntoFile("  QuerySuccessful");
	}

	@Override
	public void onQueryError(Object expected, Object real) {
		logIntoFile("  QueryError");
	}
}
