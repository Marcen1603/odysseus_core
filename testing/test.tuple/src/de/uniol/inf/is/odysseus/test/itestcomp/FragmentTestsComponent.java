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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.test.TupleTestActivator;
import de.uniol.inf.is.odysseus.test.runner.ITestComponent;


public class FragmentTestsComponent implements ITestComponent, BundleListener {

	@SuppressWarnings("unused")
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
	
	private BlockingQueue<Bundle> bundleQueue = new LinkedBlockingQueue<Bundle>();
	
	private List<Long> alreadyQueued = new ArrayList<Long>();	
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	private boolean isExecuting;
	
	private static Writer logFileWriter;	
	private static File file  = new File("FragmentTestsComponent.txt");

	@Override
	public void setUp() {
		logIntoFile("----------------------------------------------------------------------------" + NEWLINE);
		logIntoFile("SET UP " + this.getClass().getSimpleName() + " AT " + new Date().toString() + NEWLINE);
		// read all installed bundles and check for tests-folder
		for(Bundle b : TupleTestActivator.context.getBundles()){
			checkForTestsAndQueueThem(b);
		}		
		
		// ... install bundlelistener for bundles that are installed later
		TupleTestActivator.context.addBundleListener(this);		
	}
	
	private void checkForTestsAndQueueThem(Bundle bundle) {
		if(alreadyQueued.contains(bundle.getBundleId())){
			logIntoFile("BUNDLE ALREADY TESTED BEFORE - " + bundle.getSymbolicName() + "(" + bundle.getBundleId() + ")" + NEWLINE);
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
				
				if(bundleQueue.offer(bundle))
					alreadyQueued.add(bundle.getBundleId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object startTesting(String[] args) {
		logIntoFile("STARTED " + this.getClass().getSimpleName() + " AT " + new Date().toString()  + NEWLINE);

		if(isExecuting){
			logIntoFile("ALREADY EXECUTING" + NEWLINE);
			return "Already Executing";
		}
		
		isExecuting = true;
		
		Thread consumer = new Thread(new TestConsumer(bundleQueue, executor, parser));
		
		consumer.start();

		return "Running";
	}

	public static synchronized void logIntoFile(String line) {
		try {
			logFileWriter = new BufferedWriter(new FileWriter(file, true));
		
			logFileWriter.write("[" + System.nanoTime() + "] " + line);
		
			logFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		checkForTestsAndQueueThem(event.getBundle());		
	}
}
