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
package de.uniol.inf.is.odysseus.test.operator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.equivalentoutput.EquivalentOutputStarter;
import de.uniol.inf.is.odysseus.equivalentoutput.enums.StatusCode;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;

/**
 * @author Merlin Wasmann
 *
 */
public class OperatorTestComponent implements ITestComponent, IPlanModificationListener {
	
	private static final String OPERATOR_TEST_NAME = "Operator Test";
	private static final int PROCESSING_WAIT_TIME = 1000;
	private static Logger LOG = LoggerFactory.getLogger(OperatorTestComponent.class);
	
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;

	private static final String NEWLINE = System.getProperty("line.separator");
	private boolean processingDone;
	private String errortext;

	private BufferedWriter out;

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.test.runner.ITestComponent#setUp()
	 */
	@Override
	public void setUp() {

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.test.runner.ITestComponent#startTesting(java.lang.String[])
	 */
	@Override
	public Object startTesting(String[] args) {
		checkNotNull(executor, "Executor must be bound");
		checkNotNull(parser, "ScriptParser must be bound");
		checkNotNull(UserManagementProvider.getSessionmanagement(), "Session management not set");
		checkArgument(args.length == 3, "OperatorTest needs exactly arguments: [User], [Password], [FolderWithQueryFolders]");
		
		ISession session = UserManagementProvider.getSessionmanagement().login(args[0], args[1].getBytes(), UserManagementProvider.getDefaultTenant());
		
		out = createWriter(args[2]);

		// multiple folders with queries are possible
		Map<String, Triple<File, List<File>, List<File>>> queryFolders = getQueries("testqueryfolder");
		
		tryStartExecutor(executor);

		executor.addPlanModificationListener(this);
		
		LOG.debug("Processing {} queries...", queryFolders.size());
		for(String queryFolder : queryFolders.keySet()) {
			Triple<File, List<File>, List<File>> query = queryFolders.get(queryFolder);
			processingDone = false;
			try {
				String queryText = replacePaths(getQueryString(query.getFirst()), query.getSecond(), query.getThird());
			
				test(queryFolder, queryText, query.getSecond(), parser, session);
				waitProcessing();
				
				evaluateResults(queryFolder, query.getSecond());
				
				checkForErrors(errortext);
			}catch (OdysseusScriptException e) {
				LOG.error("Query {} failed! ", queryFolder, e);
				tryWrite(out, "Query " + queryFolder + " failed! " + NEWLINE + e.getMessage() + NEWLINE);
			} catch( IOException e ) {
                LOG.error("Query {} failed! ", queryFolder, e);
                tryWrite(out, "Query " + queryFolder + " failed! " + NEWLINE + e.getMessage() + NEWLINE);
			} finally {
	             executor.removeAllQueries(session);
			}
			tryFlush(out);
		}
		
		tryClose(out);
		tryStopExecutor(executor);
		
		LOG.debug("Testing finished");
		return "Success";
	}

	private void test(String key, String queryText, List<File> results, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {
		checkNotNull(results, "Results must not be null or empty");
		
		String text = "Testing query " + key + " with results from files " + results;
		LOG.debug(text);
		tryWrite(out, text + NEWLINE);
		
		parser.parseAndExecute(queryText, user, null);
	}
	
	private void evaluateResults(String key, List<File> results) {
		LOG.debug("Evaluating results of {}", key);
		Collection<Pair<String, String>> resultPairs = getResultPairs(key, results);
		
		for(Pair<String, String> pair : resultPairs) {
			List<StatusCode> codes = EquivalentOutputStarter.check(new String[]{pair.getE1(), pair.getE2()}, false);
			for(StatusCode code : codes) {
				if(code == StatusCode.EQUIVALENT_FILES) {
					break;
				} else {
					tryWrite(out, code.toString());
					if (errortext == null) {
						errortext = code.toString() + NEWLINE;
					} else {
						errortext += code.toString() + NEWLINE;
					}
				}
			}
		}
	}
	
	public void bindExecutor(IExecutor executor) {
		checkArgument(executor instanceof IServerExecutor, "Executor must be instance of " + IServerExecutor.class.getName() + " instead of " + executor.getClass().getName());
		this.executor = (IServerExecutor) executor;
	}
	
	public void bindScriptParser(IOdysseusScriptParser parser) {
		this.parser = parser;
	}
	
	/**
	 * Replaces all occurences of $input<i> and $output<j> with the absolute path to the file.
	 * 
	 * @param queryText
	 * @param results
	 * @param inputs
	 * @return
	 */
	private static String replacePaths(String queryText, List<File> results, List<File> inputs) {
		Map<Integer, String> resultMap = Maps.newHashMap();
		Map<Integer, String> inputMap = Maps.newHashMap();
		for(File result : results) {
			int number = getOutputNumber(result.getName());
			resultMap.put(number, result.getAbsolutePath().replace("expected_", ""));
		}
		for(File input : inputs) {
			int number = getOutputNumber(input.getName());
			inputMap.put(number, input.getAbsolutePath());
		}
		
		for(int i = 0; i < inputMap.size(); i++) {
			queryText = queryText.replace("$input" + i, inputMap.get(i));
		}
		
		for(int i = 0; i < resultMap.size(); i++) {
			queryText = queryText.replace("$output" + i, resultMap.get(i));
		}
		
		return queryText;
	}
	
	/**
	 * Returns the needed parts of a query for a test.
	 * Triple contains: 
	 * 	first: Query text
	 * 	second: Expected output text
	 * 	third: Input text
	 * @param directory
	 * @return
	 */
	private static Map<String, Triple<File, List<File>, List<File>>> getQueries(
			String directory) {
		checkNotNull(directory, "Queryfolders must be set");
		Map<String, File> queryFolders = getQueryFolders(directory);
		Map<String, Triple<File, List<File>, List<File>>> queries = Maps
				.newHashMap();
		
		for (String name : queryFolders.keySet()) {
			Triple<File, List<File>, List<File>> queryPart = getQueryParts(queryFolders
					.get(name));
			if (queryPart != null) {
				queries.put(name, queryPart);
			}
		}
		return queries;
	}
	
	private static Map<String, File> getQueryFolders(String directory) {
		checkNotNull(directory, "Queryfolders must be set");
		Map<String, File> queryFolders = Maps.newHashMap();
		File dir = new File(directory);
		File[] folders = dir.listFiles();
		for(File folder : folders) {
			// only directories are allowed here
			if(folder.isDirectory() && folder.listFiles().length != 0) {
				queryFolders.put(folder.getAbsolutePath(), folder);
			}
		}
		return queryFolders;
	}
	
	/**
	 * Returns the different parts needed for the test as a triple.
	 * First: Querytext-File
	 * Second: Expected output-Files
	 * Third: Input-File
	 * @param directory
	 * @return
	 */
	private static Triple<File, List<File>, List<File>> getQueryParts(File directory) {
		checkNotNull(directory, "Queryfolder must not be null");
		File[] files = directory.listFiles();
		File first = null;
		List<File> second = Lists.newArrayList();
		List<File> third = Lists.newArrayList();
		for(File file : files) {
			if(file.isDirectory()) {
				LOG.error("No directories should be in the queryfolders ({})", file);
				continue;
			}
			// catch the query
			if(file.getName().endsWith(".qry")) {
				first = file;
				continue;
			}
			// catch output
			if(file.getName().contains("expected_output")) {
				second.add(file);
				continue;
			}
			if(file.getName().contains("input")) {
				third.add(file);
				continue;
			}
		}
		if(first == null || second.isEmpty()) {
			return null;
		}
		return new Triple<File, List<File>, List<File>>(first, second, third);
	}
	
	/**
	 * Returns a collection of pairs of results. 
	 * First is the path to the expected result.
	 * Second is the path to the actual result.
	 * 
	 * @param folder
	 * @param results
	 * @return
	 */
	private static Collection<Pair<String, String>> getResultPairs(String folder, List<File> results) {
		Map<Integer, Pair<String, String>> resultPairs = Maps.newHashMap();
		for(File result : results) {
			int number = getOutputNumber(result.getName());
			resultPairs.put(number, new Pair<String, String>(result.getAbsolutePath(), null));
		}
		File dir = new File(folder);
		File[] outputs = dir.listFiles();
		for(File output : outputs) {
			if (!output.getName().contains("expected_output")
					&& output.getName().contains("output")) {
				int number = getOutputNumber(output.getName());
				if (resultPairs.containsKey(number)) {
					resultPairs.get(number).setE2(output.getAbsolutePath());
				}
			}
		}
		return resultPairs.values(); 
	}
	
	/**
	 * returns the number contained in the filename.
	 * 
	 * @param name
	 * @return
	 */
	private static int getOutputNumber(String name) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if(c < 58 && c >= 48) {
				sb.append(c);
			}
		}
		return Integer.parseInt(sb.toString());
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStartExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Starting executor");
			executor.startExecution();
		} catch (PlanManagementException e1) {
			throw new RuntimeException(e1);
		}
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStopExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Stopping executor");
			executor.stopExecution();
		} catch (PlanManagementException e) {
			LOG.error("Exception during stopping executor", e);
		}
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param errorText
	 */
	private static void checkForErrors(String errorText ) {
		if( errorText != null ) {
			throw new RuntimeException(errorText);
		}
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param query
	 * @return
	 * @throws IOException
	 */
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
	
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 *  
	 * @param directory
	 * @return
	 */
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

	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @throws OdysseusScriptException
	 */
	private void waitProcessing() throws OdysseusScriptException {
		synchronized (this) {
			while (!processingDone) {
                    try {
                        this.wait(PROCESSING_WAIT_TIME);
                    }
                    catch (InterruptedException e) {
                        throw new OdysseusScriptException(e);
                    }
			}
		}
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param out
	 */
	private static void tryFlush(BufferedWriter out) {
        try {
            out.flush();

        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param out
	 */
	private static void tryClose(BufferedWriter out) {
		try {
			out.flush();
			out.close();

		} catch (IOException e) {
		    LOG.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param out
	 * @param text
	 */
	private static void tryWrite(BufferedWriter out, String text) {
		try {
			out.write(text);
		} catch( IOException ignored ) {
		    LOG.error(ignored.getMessage(),ignored);
		}
	}
	
	@Override
	public String toString() {
		return OPERATOR_TEST_NAME;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener#planModificationEvent(de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent)
	 */
	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if(eventArgs.getEventType() == PlanModificationEventType.QUERY_STOP) {
			processingDone = true;
		}
	}
	
}
