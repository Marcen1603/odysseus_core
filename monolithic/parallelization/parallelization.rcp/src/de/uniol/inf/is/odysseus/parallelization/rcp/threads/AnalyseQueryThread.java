package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.InterOperatorParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.commands.IEditorTextParserConstants;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionErrorDialog;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class AnalyseQueryThread extends Thread {

	private static Logger LOG = LoggerFactory
			.getLogger(AnalyseQueryThread.class);

	private final InterOperatorParallelizationBenchmarkerWindow window;
	private final ProgressBar progressAnalyseQuery;
	private static ISelection selection;
	private static IEditorPart part;

	public AnalyseQueryThread(
			InterOperatorParallelizationBenchmarkerWindow interOperatorParallelizationBenchmarkerWindow,
			ProgressBar progressAnalyseQuery) {
		this.window = interOperatorParallelizationBenchmarkerWindow;
		this.progressAnalyseQuery = progressAnalyseQuery;
	}

	@Override
	public void run() {
		LOG.debug("Analysing of current query started.");

		Display.getDefault().asyncExec(new Runnable() {


			@Override
			public void run() {
				part = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				selection = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getSelectionService()
						.getSelection();
			}
		});

		int counter = 0;
		while (selection == null) {
			if (counter == 1000) {
				break;
			}
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
		}

		if (selection != null) {
			doQueryAnalysis();
		}

		changeProgressBarSelection(20);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		changeProgressBarSelection(40);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		changeProgressBarSelection(60);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		changeProgressBarSelection(80);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		changeProgressBarSelection(100);
		changeWindowOnSuccess();
		LOG.debug("Analysing of current query done.");
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
				progressAnalyseQuery.setSelection(selectionValue);
			}

		});
	}

	private void doQueryAnalysis() {
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

		if (selection instanceof IStructuredSelection) {
			List<Object> selectedObjects = ((IStructuredSelection) selection)
					.toList();

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
		if (part instanceof OdysseusScriptEditor) {
			return Optional.of((OdysseusScriptEditor) part);
		}
		return Optional.absent();
	}

	private void runFile(IFile fileToRun) {
		runFiles(Lists.newArrayList(fileToRun));
	}

	private void runFiles(List<IFile> filesToRun) {
		try {
			for (IFile file : filesToRun) {
				execute(file);
			}
		} catch (Exception ex) {
			LOG.error("Exception during running query file", ex);
			ExceptionErrorDialog
					.open(new Status(
							IStatus.ERROR,
							IEditorTextParserConstants.PLUGIN_ID,
							"'Parsing and Executing Query' has encountered a problem.\n\nScript Execution Error: "
									+ ex.getMessage(), ex), ex);
			// new ExceptionWindow(ex);
		}

	}

	private static void execute(final IFile scriptFile) {
		Job job = new Job("Parsing and Executing Query") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					String text = readLinesFromFile(scriptFile);
					IExecutor executor = OdysseusRCPEditorTextPlugIn
							.getExecutor();
					executor.addQuery(text, "OdysseusScript",
							OdysseusRCPPlugIn.getActiveSession(),
							ParserClientUtil.createRCPContext(scriptFile));
				} catch (final Throwable ex) {
					ExceptionErrorDialog
							.open(new Status(
									IStatus.ERROR,
									IEditorTextParserConstants.PLUGIN_ID,
									"'Parsing and Executing Query' has encountered a problem.\n\nScript Execution Error: "
											+ ex.getMessage(), ex), ex);
					// return new Status(IStatus.ERROR,
					// IEditorTextParserConstants.PLUGIN_ID,
					// "Script Execution Error: " + ex.getMessage(), ex);
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
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
			lines = lines + line;
			line = br.readLine();
			lines = lines + System.lineSeparator();
		}
		br.close();
		return lines;
	}
}
