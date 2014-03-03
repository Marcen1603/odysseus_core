package de.uniol.inf.is.odysseus.rcp.evaluation.command;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.evaluation.Activator;

public class StartEvaluationCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (Activator.getExecutor() != null) {
			for (IFile file : getSelectedFiles()) {
				try {
					Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					runIt(file, shell);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("EXECUTOR NOT BOUND!!");
		}
		return null;
	}

	private static List<IFile> getSelectedFiles() {
		List<IFile> foundFiles = new ArrayList<>();
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			List<?> selectedObjects = ((IStructuredSelection) selection).toList();
			for (Object obj : selectedObjects) {
				if (obj instanceof IFile) {
					IFile f = (IFile) obj;
					if (f.getFileExtension().endsWith("qry")) {
						foundFiles.add(f);
					}
				}
			}
		}

		if (foundFiles.isEmpty()) {
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (part instanceof OdysseusScriptEditor) {
				if (part.getEditorInput() instanceof FileEditorInput) {
					FileEditorInput input = (FileEditorInput) part.getEditorInput();
					IFile f = input.getFile();
					if (f.getFileExtension().endsWith("qry")) {
						foundFiles.add(f);
					}
				}
			}
		}
		return foundFiles;
	}

	private void runIt(IFile file, Shell parentShell) throws Exception {
		if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		int times = 10;
		Map<String, List<String>> values = new TreeMap<>();
		EvaluationSettingsDialog esd = new EvaluationSettingsDialog(parentShell);
		if (esd.open() == Window.OK) {
			times = esd.getTimes();
			values = esd.getActiveValues();
			// read lines
			String lines = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents()));
			String line = br.readLine();
			while (line != null) {
				lines = lines + line + "\n";
				line = br.readLine();
			}
			br.close();
			// run job
			if (!lines.isEmpty()) {
				Job job = new EvaluationJob("Running Evaluation...", values, times, lines, file);
				job.setUser(true);
				job.schedule();
			}
		}
	}
}
