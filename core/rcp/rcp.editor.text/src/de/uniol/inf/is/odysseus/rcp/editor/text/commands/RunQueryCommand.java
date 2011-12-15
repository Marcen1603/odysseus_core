/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.windows.ExceptionWindow;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class RunQueryCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();

			if (obj instanceof IFile) {
				run((IFile) obj);
				return null;
			}
		}

		// Check if we have an active Editor
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if (part instanceof OdysseusScriptEditor) {
			OdysseusScriptEditor editor = (OdysseusScriptEditor) part;
			FileEditorInput input = (FileEditorInput) editor.getEditorInput();
			run(input.getFile());
			return null;
		}

		return null;
	}

	private void run(IFile queryFile) {
		try {
			// Datei öffnen
			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO))
				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);

			// Datei einlesen
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(queryFile.getContents()));
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			br.close();

			execute(lines.toArray(new String[lines.size()]));

		} catch (Exception ex) {
			new ExceptionWindow(ex);
		}
	}

	public void execute(final String[] text) {
		// Dieser Teil geschieht asynchron zum UIThread und wird als Job
		// ausgeführt
		// Job-Mechanismus wird von RCP gestellt.
		Job job = new Job("Parsing and Executing Query") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus status = Status.OK_STATUS;
				try {
					User user = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
					// Befehle holen
					final List<PreParserStatement> statements = OdysseusRCPEditorTextPlugIn.getScriptParser().parseScript(text, user);

					// Erst Text testen
					monitor.beginTask("Executing Commands", statements.size() * 2);
					monitor.subTask("Validating");

					Map<String, Object> variables = new HashMap<String, Object>();
					for (PreParserStatement stmt : statements) {
						stmt.validate(variables, user);
						monitor.worked(1);

						// Wollte der Nutzer abbrechen?
						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;
					}

					// Dann ausführen
					variables = new HashMap<String, Object>();
					int counter = 1;
					for (PreParserStatement stmt : statements) {
						monitor.subTask("Executing (" + counter + " / " + statements.size() + ")");
						stmt.execute(variables, user, OdysseusRCPEditorTextPlugIn.getScriptParser());
						monitor.worked(1);
						counter++;
					}
				} catch (OdysseusScriptException ex) {
					Throwable cause = ex.getCause();
					if (cause != null && cause instanceof QueryParseException){
						status = new Status(Status.ERROR, IEditorTextParserConstants.PLUGIN_ID, "Parse Error: "+cause.getMessage(), cause);
					}else{
						status = new Status(Status.ERROR, IEditorTextParserConstants.PLUGIN_ID, "Execution Error: "+ex.getMessage(), ex);
					}										
				} 
				monitor.done();
			
				return status;
			}
		};
		job.setUser(true); // gibt an, dass der Nutzer dieses Job ausgelöst hat
		job.schedule(); // dieser Job soll nun ausgeführt werden
	}

}
