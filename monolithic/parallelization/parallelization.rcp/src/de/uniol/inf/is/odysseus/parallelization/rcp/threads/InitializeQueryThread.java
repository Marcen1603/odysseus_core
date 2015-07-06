package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parallelization.benchmark.transformationhandler.BenchmarkPreTransformationHandler;
import de.uniol.inf.is.odysseus.parallelization.interoperator.keyword.InterOperatorParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.helper.BenchmarkHelper;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;
import de.uniol.inf.is.odysseus.script.keyword.PreTransformationHandlerPreParserKeyword;

public class InitializeQueryThread extends Thread {

	private static Logger LOG = LoggerFactory
			.getLogger(InitializeQueryThread.class);

	private final ParallelizationBenchmarkerWindow window;
	private final ProgressBar progressInitializeQuery;
	private static BenchmarkDataHandler benchmarkDataHandler;

	private boolean errorOccoured = false;

	public InitializeQueryThread(
			ParallelizationBenchmarkerWindow parallelizationBenchmarkerWindow,
			ProgressBar progressAnalyseQuery) {
		this.window = parallelizationBenchmarkerWindow;
		this.progressInitializeQuery = progressAnalyseQuery;
	}

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
			changeProgressBarSelection(20);

			// if we have this values, we could start executing the current
			// query
			if (benchmarkDataHandler.getSelection() != null) {
				runSelectedQuery();
			}
			changeProgressBarSelection(50);

			// if executing of query works, we can start analysing the logical
			// plan
			if (benchmarkDataHandler.getLogicalQuery() != null) {
				BenchmarkHelper.getPossibleParallelizationOptions(
						benchmarkDataHandler.getLogicalQuery(),
						benchmarkDataHandler.getUniqueIdentifier());
			} else {
				throw new Exception("Logical query not found");
			}
		} catch (Exception e) {
			createError(e);
		}

		changeProgressBarSelection(100);

		if (!errorOccoured) {
			changeWindowOnSuccess();
		}

		benchmarkDataHandler = null;

		LOG.debug("Initializing of current query done.");
	}

	private void getWorkbenchData() {
		// getting values from workbench, needs asynchonious execution, because
		// its inside of another view
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				benchmarkDataHandler.setPart(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor());
				benchmarkDataHandler.setSelection(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getSelectionService()
						.getSelection());
			}
		});

		// wait until values are found
		int counter = 0;
		while (benchmarkDataHandler.getSelection() == null) {
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

	private void changeWindowOnSuccess() {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.clearPageContent();
				window.createConfigContent();
			}

		});
	}

	private void changeProgressBarSelection(final int selectionValue) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				progressInitializeQuery.setSelection(selectionValue);
			}

		});
	}

	private void createError(final Throwable ex) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.createErrorMessage(ex);
			}
		});
		errorOccoured = true;
	}

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

	@SuppressWarnings("unchecked")
	private static List<IFile> getSelectedFiles() {
		List<IFile> foundFiles = Lists.newArrayList();
		if (benchmarkDataHandler.getSelection() instanceof IStructuredSelection) {
			List<Object> selectedObjects = ((IStructuredSelection) benchmarkDataHandler
					.getSelection()).toList();
			for (Object obj : selectedObjects) {
				if (isQueryTextFile(obj)) {
					foundFiles.add((IFile) obj);
				}
			}
		}
		return foundFiles;
	}

	private static boolean isQueryTextFile(Object obj) {
		return obj instanceof IFile
				&& ((IFile) obj).getFileExtension().equals(
						OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION);
	}

	private static Optional<OdysseusScriptEditor> getScriptEditor() {
		if (benchmarkDataHandler.getPart() instanceof OdysseusScriptEditor) {
			return Optional.of((OdysseusScriptEditor) benchmarkDataHandler
					.getPart());
		}
		return Optional.absent();
	}

	private void runFile(IFile fileToRun) throws CoreException, IOException {
		runFiles(Lists.newArrayList(fileToRun));
	}

	private void runFiles(List<IFile> filesToRun) throws CoreException,
			IOException {
		for (IFile file : filesToRun) {
			execute(file);
		}
	}

	private void execute(final IFile scriptFile) throws CoreException,
			IOException {

		benchmarkDataHandler.setQueryFile(scriptFile);
		String text = readLinesFromFile(scriptFile);
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		Collection<Integer> queryIds = executor.addQuery(text,
				"OdysseusScript", OdysseusRCPPlugIn.getActiveSession(),
				ParserClientUtil.createRCPContext(scriptFile));
		if (queryIds.size() > 0) {
			Integer queryId = queryIds.iterator().next();
			ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryId,
					OdysseusRCPPlugIn.getActiveSession());
			benchmarkDataHandler.setLogicalQuery(logicalQuery);
			executor.removeQuery(queryId, OdysseusRCPPlugIn.getActiveSession());
		}
	}

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
				benchmarkDataHandler.addQueryString(line
						+ System.lineSeparator());
				lines = lines + line;
				lines = lines + System.lineSeparator();
			}

			line = br.readLine();
		}
		br.close();
		return lines;
	}

	private static boolean isLineSupported(String line) {
		// #PARALLELIZATION is not supported in benchmarker
		if (!line.trim().startsWith(
				"#" + InterOperatorParallelizationPreParserKeyword.KEYWORD)) {
			return false;
		}

		// #INTEROPERATORPARALLELIZATION is not supported in benchmarker
		if (!line.trim().startsWith(
				"#" + ParallelizationPreParserKeyword.KEYWORD)) {
			return false;
		}

		// PRETRANSFORMATION for benchmarking is also not supported
		if (!line.trim().startsWith(
				"#" + PreTransformationHandlerPreParserKeyword.KEYWORD + " "+BenchmarkPreTransformationHandler.NAME)) {
			return false;
		}

		return true;
	}
}
