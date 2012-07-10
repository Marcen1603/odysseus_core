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
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;

/**
 * Test Component for Query-Tests with arbitrary Tuples
 * 
 * @author Kai Pancratz, Alexander Funk
 *
 */
public class TupleTestComponent {

	private static Logger LOG = LoggerFactory.getLogger(TupleTestComponent.class);

	private IServerExecutor executor;
	private IOdysseusScriptParser parser;
	private ISessionManagement sessionManagement;	
	
	private static final String NEWLINE = System.getProperty("line.separator");
	private String errorText;

	private BufferedWriter out;
	private long startTime;

	public void activate(ComponentContext context) {
		startTesting();		
	}

	public void bindExecutor(IExecutor executor) {
		checkArgument(executor instanceof IServerExecutor, "Executor must be instance of " + IServerExecutor.class.getName() + " instead of " + executor.getClass().getName());
		this.executor = (IServerExecutor) executor;
	}

	public void bindScriptParser(IOdysseusScriptParser scriptParser) {
		this.parser = scriptParser;
	}
	
	public void bindSessionManagement(ISessionManagement sessionManagement){
		this.sessionManagement = sessionManagement;
	}
	
	public void unbindExecutor(IExecutor executor){
		
	}

	public void unbindScriptParser(IOdysseusScriptParser scriptParser){
		
	}
	
	public void unbindSessionManagement(ISessionManagement sessionManagement){
		
	}
	
	public void startTesting() {
		ISession session = sessionManagement.login("System", "manager".getBytes());

		Map<String, List<File>> querys = readQueries();
		
		tryStartExecutor(executor);
		
		out = createWriter(TupleTestActivator.getBundlePath().getAbsolutePath());
		
		LOG.debug("Processing queries ...");
		
		for(Entry<String, List<File>> groupQuery : querys.entrySet()){
			LOG.debug("Starting tests from group: {}", groupQuery.getKey());
			
			for(File qry : groupQuery.getValue()){
				try {
					test(qry, parser, session);
					//executor.removeAllQueries(session);
					
					checkForErrors(errorText);
					LOG.debug("Query {} finished", qry.getName());

				} catch (OdysseusScriptException e) {
					LOG.error("Query {} failed! ", qry.getName(), e);
					tryWrite(out, "Query " + qry.getName() + " failed! " + NEWLINE + e.getMessage());
				} catch( IOException e ) {
	                LOG.error("Query {} failed! ", qry.getName(), e);
	                tryWrite(out, "Query " + qry.getName() + " failed! " + NEWLINE + e.getMessage());
				}
			}
			
			LOG.debug("Finished tests from group: {}", groupQuery.getKey());
		}
		
		tryClose(out);
		tryStopExecutor(executor);
		LOG.error("Testing finished");
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
			//LOG.debug("Starting executor");
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}
	}

	private static void tryStopExecutor(IServerExecutor executor) {
		try {
//			LOG.debug("Stopping executor");
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
				
		String path = "#DEFINE PATH " + TupleTestActivator.bundlePath + " \n";
		
		List<PreParserStatement> statements = parser.parseScript(path + getQueryString(query), user);
		
		startTime = System.nanoTime();
		parser.execute(statements, user, null);
		
		
		// TODO		
//		executor.startQuery(0, user);
//		executor.removeQuery(0, user);
		
//		parser.parseAndExecute(path + getQueryString(query), user, null);
		LOG.debug("Start Query");
		executor.startQuery(0, user);
		LOG.debug("Stop Query");
		executor.removeQuery(0, user);
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
}
