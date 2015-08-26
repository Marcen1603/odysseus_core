/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.benchmark.transformationhandler.BenchmarkPreTransformationHandler;
import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword.IntraOperatorParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.helper.BenchmarkHelper;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite.BenchmarkStartComposite;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;
import de.uniol.inf.is.odysseus.script.keyword.PreTransformationHandlerPreParserKeyword;

/**
 * Thread for initializing currently selected query
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkInitializeQueryThread extends Thread {

	private static Logger LOG = LoggerFactory
			.getLogger(BenchmarkInitializeQueryThread.class);

	private final ParallelizationBenchmarkerWindow window;
	private static BenchmarkDataHandler benchmarkDataHandler;

	private boolean errorOccoured = false;

	public BenchmarkInitializeQueryThread(
			ParallelizationBenchmarkerWindow parallelizationBenchmarkerWindow) {
		this.window = parallelizationBenchmarkerWindow;
	}

	/**
	 * run method for this thread. Get query from editor, modify it and execute
	 * it.
	 */
	@Override
	public void run() {
		LOG.debug("Initializing of current query started.");

		try {
			// create data handler for benchmark
			benchmarkDataHandler = BenchmarkDataHandler.getNewInstance();
			window.setBenchmarkProcessId(benchmarkDataHandler
					.getUniqueIdentifier());

			// get RCP data for query script
			getWorkbenchData();
			updateProgress(20);

			// if we have this values, we could start executing the current
			// query
			if (benchmarkDataHandler.getBenchmarkInitializationResult()
					.getSelection() != null) {
				runSelectedQuery();
			}
			updateProgress(50);

			// if executing of query works, we can start analysing the logical
			// plan
			if (!benchmarkDataHandler.getBenchmarkInitializationResult()
					.getLogicalQueries().isEmpty()) {
				BenchmarkHelper
						.getPossibleParallelizationOptions(benchmarkDataHandler);
			} else {
				throw new Exception("Logical query not found");
			}
		} catch (Exception e) {
			createError(e);
		}

		updateProgress(100);

		if (!errorOccoured) {
			changeWindowOnSuccess();
		}

		benchmarkDataHandler = null;

		LOG.debug("Initializing of current query done.");
	}

	/**
	 * gets the workbench data
	 */
	private void getWorkbenchData() {
		// getting values from workbench, needs asynchonious execution, because
		// its inside of another view
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				benchmarkDataHandler.getBenchmarkInitializationResult()
						.setPart(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage().getActiveEditor());
				benchmarkDataHandler.getBenchmarkInitializationResult()
						.setSelection(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getSelectionService().getSelection());
			}
		});

		// wait until values are found
		int counter = 0;
		while (benchmarkDataHandler.getBenchmarkInitializationResult()
				.getSelection() == null) {
			if (counter == 1000) {
				break;
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
		}
	}

	/**
	 * change the window (go to configuration page)
	 */
	private void changeWindowOnSuccess() {
		// switch to UI thread
		if (!window.getWindow().isDisposed()) {
			window.getWindow().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {

					window.createConfigContent();
				}

			});
		}
	}

	/**
	 * updates the current progress (progress bar)
	 * 
	 * @param selectionValue
	 */
	private void updateProgress(final int selectionValue) {
		// switch to UI thread
		if (!window.getWindow().isDisposed()) {
			window.getWindow().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					BenchmarkStartComposite benchmarkStartComposite = window
							.getStartComposite();
					benchmarkStartComposite.updateProgress(selectionValue);
				}

			});
		}
	}

	/**
	 * creates an error on UI if something went wrong
	 * 
	 * @param ex
	 */
	private void createError(final Throwable ex) {
		// switch to UI thread
		if (!window.getWindow().isDisposed()) {
			window.getWindow().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					window.createErrorMessage(ex);
				}
			});
			errorOccoured = true;
		}
	}

	/**
	 * run the selected query file
	 * 
	 * @throws CoreException
	 * @throws IOException
	 */
	private void runSelectedQuery() throws CoreException, IOException {
		List<IFile> optFileToRun = getSelectedFiles();
		if (!optFileToRun.isEmpty()) {
			runFiles(optFileToRun);
		} else {
			Optional<OdysseusScriptEditor> optEditor = getScriptEditor();
			if (optEditor.isPresent()) {
				OdysseusScriptEditor editor = optEditor.get();
				runFile(editor.getFile());
			}
		}
	}

	/**
	 * returns all selected files
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<IFile> getSelectedFiles() {
		List<IFile> foundFiles = Lists.newArrayList();
		if (benchmarkDataHandler.getBenchmarkInitializationResult()
				.getSelection() instanceof IStructuredSelection) {
			List<Object> selectedObjects = ((IStructuredSelection) benchmarkDataHandler
					.getBenchmarkInitializationResult().getSelection())
					.toList();
			for (Object obj : selectedObjects) {
				if (isQueryTextFile(obj)) {
					foundFiles.add((IFile) obj);
				}
			}
		}
		return foundFiles;
	}

	/**
	 * checks if the selected file is a query
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isQueryTextFile(Object obj) {
		return obj instanceof IFile
				&& ((IFile) obj).getFileExtension().equals(
						OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION);
	}

	/**
	 * gets the odysseus script editor if exists
	 * 
	 * @return
	 */
	private static Optional<OdysseusScriptEditor> getScriptEditor() {
		if (benchmarkDataHandler.getBenchmarkInitializationResult().getPart() instanceof OdysseusScriptEditor) {
			return Optional.of((OdysseusScriptEditor) benchmarkDataHandler
					.getBenchmarkInitializationResult().getPart());
		}
		return Optional.absent();
	}

	/**
	 * run a single file
	 * 
	 * @param fileToRun
	 * @throws CoreException
	 * @throws IOException
	 */
	private void runFile(IFile fileToRun) throws CoreException, IOException {
		runFiles(Lists.newArrayList(fileToRun));
	}

	/**
	 * run multiple files
	 * 
	 * @param filesToRun
	 * @throws CoreException
	 * @throws IOException
	 */
	private void runFiles(List<IFile> filesToRun) throws CoreException,
			IOException {
		for (IFile file : filesToRun) {
			execute(file);
		}
	}

	/**
	 * do the execution of the selected query
	 * 
	 * @param scriptFile
	 * @throws CoreException
	 * @throws IOException
	 */
	private void execute(final IFile scriptFile) throws CoreException,
			IOException {

		benchmarkDataHandler.getBenchmarkInitializationResult().setQueryFile(
				scriptFile);
		String text = readLinesFromFile(scriptFile);
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		ISession activeSession = OdysseusRCPPlugIn.getActiveSession();
		Collection<Integer> queryIds = executor.addQuery(text,
				"OdysseusScript", activeSession,
				ParserClientUtil.createRCPContext(scriptFile));
		// if we have a query
		if (queryIds.size() > 0) {
			List<Integer> queryIdArray = new ArrayList<Integer>(queryIds);
			for (Integer queryId : queryIdArray) {
				QueryState state = executor.getQueryState(queryId,
						activeSession);
				// if query is inactive, start it
				if (state == QueryState.INACTIVE) {
					executor.startQuery(queryId, activeSession);
				}
				// get logical plan from executor
				ILogicalQuery logicalQuery = executor.getLogicalQueryById(
						queryId, activeSession);
				benchmarkDataHandler.getBenchmarkInitializationResult()
						.addLogicalQuery(logicalQuery);
			}

			// if we have all logical plans, we need to stop and remove all
			// querys
			for (Integer queryId : queryIdArray) {
				executor.removeQuery(queryId, activeSession);
			}
		}
	}

	/**
	 * reads the text for the query and removes odysseus script keywords used in
	 * parallelization
	 * 
	 * @param queryFile
	 * @return queryString
	 * @throws CoreException
	 * @throws IOException
	 */
	private static String readLinesFromFile(IFile queryFile)
			throws CoreException, IOException {
		if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
			queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
		}

		String lines = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				queryFile.getContents()));

		String line = br.readLine();
		while (line != null) {
			// Remove parallelization keywords
			if (isLineSupported(line)) {
				benchmarkDataHandler.getBenchmarkInitializationResult()
						.addQueryString(line + System.lineSeparator());
				lines = lines + line;
				lines = lines + System.lineSeparator();
			}

			line = br.readLine();
		}
		br.close();
		return lines;
	}

	/**
	 * checks if the line is supported (lines with parallelization or
	 * benchmarker keywords are not supported)
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isLineSupported(String line) {
		// #PARALLELIZATION is not supported in benchmarker
		if (line.trim().startsWith(
				"#" + ParallelizationPreParserKeyword.KEYWORD)) {
			return false;
		}

		// #INTEROPERATORPARALLELIZATION is not supported in benchmarker
		if (line.trim().startsWith(
				"#" + InterOperatorParallelizationPreParserKeyword.KEYWORD)) {
			return false;
		}

		// #INTRAOPERATORPARALLELIZATION is not supported in benchmarker
		if (line.trim().startsWith(
				"#" + IntraOperatorParallelizationPreParserKeyword.KEYWORD)) {
			return false;
		}

		// PRETRANSFORMATION for benchmarking is also not supported
		if (line.trim().startsWith(
				"#" + PreTransformationHandlerPreParserKeyword.KEYWORD + " "
						+ BenchmarkPreTransformationHandler.NAME)) {
			return false;
		}

		return true;
	}
}
