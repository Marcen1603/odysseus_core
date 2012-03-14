package de.uniol.inf.is.odysseus.test;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;

/**
 * Testkomponente für Query-Tests mit Nexmark
 * 
 * @author Timo Michelsen
 *
 */
public class TestComponent implements ITestComponent, ICompareSinkListener {

	private static final String NEXMARK_TESTS_NAME = "Nexmark Tests";
	private static final int PROCESSING_WAIT_TIME = 1000;
	private static Logger LOG = LoggerFactory.getLogger(TestComponent.class);

	private IServerExecutor executor;
	private IOdysseusScriptParser parser;

	private static final String NEWLINE = System.getProperty("line.separator");
	private boolean processingDone;
	private String errorText;

	private BufferedWriter out;

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
		checkNotNull(executor, "Executor must be bound");
		checkNotNull(parser, "Parser must be bound");
		checkNotNull(UserManagement.getSessionmanagement(), "session management not set");
		checkArgument(args.length == 3, "NexmarkTest needs exactly three arguments: [User], [Password], [FolderWithQueries]");
		
		ISession session = UserManagement.getSessionmanagement().login(args[0], args[1].getBytes());

		out = createWriter(args[2]);

		Map<String, File> queries = Maps.newHashMap();
		Map<String, File> results = Maps.newHashMap();
		ImmutableList<File> fileArray = determineQueryFiles(args[2]);

		determineQueriesAndResults( fileArray, queries, results );

		tryStartExecutor(executor);

		LOG.debug("Processing " + queries.size() + " queries ...");
		for (Entry<String, File> query : queries.entrySet()) {
			
			final String queryKey = query.getKey();
			final File queryFile = query.getValue();
			processingDone = false;
			
			try {
				test(queryKey, queryFile, results.get(queryKey), parser, session);
				waitProcessing();
				
				executor.removeAllQueries(session);
				
				checkForErrors(errorText);
				LOG.debug("Query {} successfull", queryKey);

			} catch (OdysseusScriptException e) {
				LOG.error("Query {} failed! ", queryKey, e);
				tryWrite(out, "Query " + queryKey + " failed! " + NEWLINE + e.getMessage());
			} catch( IOException e ) {
                LOG.error("Query {} failed! ", queryKey, e);
                tryWrite(out, "Query " + queryKey + " failed! " + NEWLINE + e.getMessage());
			}
		}
		
		tryClose(out);
		tryStopExecutor(executor);

		LOG.debug("Testing finished");
		
		return "Success";
	}

    private static void determineQueriesAndResults(ImmutableList<File> fileArray, Map<String, File> queries, Map<String, File> results) {
        checkNotNull(fileArray, "fileArray must not be null");
        checkNotNull(queries, "Map of Queries must not be null");
        checkNotNull(results, "Map of Results must not be null");
        
        for (File file : fileArray) {
            String name = file.getName();
            int point = name.lastIndexOf(".");
            String pre = name.substring(0, point);
            String post = name.substring(point + 1);
            if (post.equalsIgnoreCase("qry")) {
                queries.put(pre, file);
            } else if (post.equalsIgnoreCase("csv")) {
                results.put(pre, file);
            }
        }        
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

	private void waitProcessing() {
		synchronized (this) {
			while (!processingDone) {
				try {
                    this.wait(PROCESSING_WAIT_TIME);
                }
                catch (InterruptedException ignored) {}
			}
		}
	}
	
	private static void checkForErrors(String errorText ) {
		if( errorText != null ) {
			throw new RuntimeException(errorText);
		}
	}

	private static ImmutableList<File> determineQueryFiles( String directory) {
	    checkNotNull(directory, "Directory for determining query files must not be null");
	    
		LOG.debug("Looking for files in " + directory);
		File f = new File(directory);
		File[] fileArray = f.listFiles();
		if (fileArray == null) {
			return ImmutableList.of();
		}

		return ImmutableList.<File> copyOf(Iterables.filter(Lists.newArrayList(fileArray), new Predicate<File>() {
			@Override
			public boolean apply(File file) {
				return file.isFile();
			}
		}));
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

	private void test(String key, File query, File result, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {
		checkNotNull(result, "ResultSet must not be null!");
		
		String text = "Testing Query " + key + " from file " + query + " with results from file " + result;
		LOG.debug(text);
		tryWrite(out, text);

		parser.parseAndExecute(getQueryString(query), user, new SimpleCompareSink(result, this));
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

	@Override
	public synchronized void processingDone() {
		LOG.debug("Query processing done");
		tryWrite(out, " ok " + NEWLINE);

		processingDone = true;
		errorText = null;
		
		notifyAll();
	}

	@Override
	public synchronized void processingError(String line, String input) {
		String text = "Query processing created error " + line + " " + input;
		LOG.error(text);
		tryWrite(out, text);

		processingDone = true;
		errorText = "Wrong Result input '" + input + "'. Expected: '" + line + "'";
		notifyAll();
	}

	@Override
	public String toString() {
		return NEXMARK_TESTS_NAME;
	}

}
