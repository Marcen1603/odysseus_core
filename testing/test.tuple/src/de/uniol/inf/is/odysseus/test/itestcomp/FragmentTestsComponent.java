package de.uniol.inf.is.odysseus.test.itestcomp;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
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

/**
 * This implementation of ITestComponent will execute all 
 * tests found in a corresponding tests-folder inside any bundle.
 * 
 * @author Timo Michelsen
 *
 */
public class FragmentTestsComponent implements ITestComponent {
	private static Logger LOG = LoggerFactory.getLogger(FragmentTestsComponent.class);

	private List<Long> alreadyTested = new ArrayList<Long>();
	
//	private SynchronizedQueue<Bundle> bundlesToTest = new SynchronizedQueue<>(100); // TODO remove capacity
	
	private List<Bundle> bundlesToTest = Collections.synchronizedList(new ArrayList<Bundle>());
	
	private IServerExecutor executor;
	private IOdysseusScriptParser parser;

	private static final String NEWLINE = System.getProperty("line.separator");
	private boolean isExecuting;
	private String errorText;

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
		// lese alle schon installierten bundles aus und führe deren tests durch
		for(Bundle b : TestBundleActivator.context.getBundles()){
			checkForTestsAndQueueThem(b);
		}		
		
		// ... außerdem installiere bundleListener um nachträglich installierte bundles zu testen
		TestBundleActivator.context.addBundleListener(new BundleListener() {
			
			@Override
			public void bundleChanged(BundleEvent event) {
				checkForTestsAndQueueThem(event.getBundle());
			}
		});		
	}
	
	private void checkForTestsAndQueueThem(Bundle bundle) {
		if (alreadyTested.contains(bundle.getBundleId())) {
			return;
		}

		// look for "tests" Folder
		URL fileUrl = bundle.getResource("tests");

		try {
			File testsDir = new File(FileLocator.toFileURL(fileUrl).getPath());

			// ... and add to queue for later testing if found
			if (testsDir != null) {
				bundlesToTest.add(bundle);
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
	
	private void executeTests(Bundle bundle){
		// diese methode soll alle entahlten tests (param) ausführen
		File testFolder = getTestsFolder(bundle);
		Map<String, List<File>> testsQueries = readQueries(testFolder);
		
		checkNotNull(testFolder, "Testfolder not found");
		checkNotNull(executor, "Executor must be bound");
		checkNotNull(parser, "Parser must be bound");
		checkNotNull(UserManagement.getSessionmanagement(), "session management not set");
		
		LOG.debug("[TESTS] Processing tests for bundle {}", bundle.toString());
		
		ISession session = UserManagement.getSessionmanagement().login("System", "manager".getBytes());
		
		out = createWriter(testFolder); // TODO write into correct directory

		tryStartExecutor(executor);		

		LOG.debug("Processing " + testsQueries.size() + " queries ...");
		for (Entry<String, List<File>> queries : testsQueries.entrySet()) {
			
			
			for(File qry : queries.getValue()){
				final String queryKey = qry.getName();
				final File queryFile = qry;
				
				try {
					test(queryKey, queryFile, parser, session);
					
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
			
		}
		
		tryClose(out);
		tryStopExecutor(executor);
		
		LOG.debug("[TESTS] Finished tests for bundle {}", bundle.toString());
	}
	
	@Override
	public synchronized Object startTesting(String[] args) {		
//		checkArgument(args.length == 3, "NexmarkTest needs exactly three arguments: [User], [Password], [FolderWithQueries]");

		if(isExecuting){
			return "Already Executing";
		}
		
		isExecuting = true;
		
		synchronized (bundlesToTest) {
		      Iterator<Bundle> i = bundlesToTest.iterator(); // Must be in synchronized block
		      while (i.hasNext())
		          executeTests(i.next());
		  }
		 
		isExecuting = false;
		
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

	private void test(String key, File query, IOdysseusScriptParser parser, ISession user) throws OdysseusScriptException, IOException {		
		String text = "Testing Query " + key + " from file " + query;
		LOG.debug(text);
		tryWrite(out, text);
		
		startTime = System.nanoTime();
		
		String path = "#DEFINE PATH " + query.getAbsolutePath() + " \n";
		
		parser.parseAndExecute(path + getQueryString(query), user, null);
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
}
