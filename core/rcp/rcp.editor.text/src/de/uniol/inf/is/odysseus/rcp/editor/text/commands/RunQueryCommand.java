/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptDocumentProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;

public class RunQueryCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RunQueryCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<IFile> optFileToRun = getSelectedFile();
		if( optFileToRun.isPresent() ) {
			run(optFileToRun.get());
		} else {
			
			Optional<OdysseusScriptEditor> optEditor = getScriptEditor();
			if( optEditor.isPresent() ) {
				OdysseusScriptEditor editor = optEditor.get();
				String[] lines = readLines(editor);
				execute(lines);
				return null;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return getSelectedFile().isPresent() || getScriptEditor().isPresent();
	}

	private static Optional<IFile> getSelectedFile() {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();

			if( isQueryTextFile(obj)) {
				return Optional.of((IFile) obj);
			}
		}
		
		return Optional.absent();
	}
	
	private static boolean isQueryTextFile(Object obj) {
		return obj instanceof IFile && ((IFile)obj).getFileExtension().equals(OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION);
	}
	
	private static Optional<OdysseusScriptEditor> getScriptEditor() {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof OdysseusScriptEditor) {
			return Optional.of((OdysseusScriptEditor) part);
		}		
		return Optional.absent();
	}

	private static String[] readLines(OdysseusScriptEditor editor) {
		OdysseusScriptDocumentProvider docPro = (OdysseusScriptDocumentProvider) editor.getDocumentProvider();
		Document doc = (Document) docPro.getDocument(editor.getEditorInput());
		String text = doc.get();
		String lines[] = text.split(doc.getDefaultLineDelimiter());
		return lines;
	}
	
	private static void run(IFile queryFile) {
		try {
			// Datei �ffnen
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
			LOG.error("Exception during running query file" , ex);
			
			new ExceptionWindow(ex);
		}
	}

	private static void execute(final String[] text) {
		Job job = new Job("Parsing and Executing Query") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus status = Status.OK_STATUS;
				try {
					ISession user = OdysseusRCPPlugIn.getActiveSession();
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

					// Dann ausf�hren
					variables = new HashMap<String, Object>();
					int counter = 1;
					for (PreParserStatement stmt : statements) {
						monitor.subTask("Executing (" + counter + " / " + statements.size() + ")");
						stmt.execute(variables, user, OdysseusRCPEditorTextPlugIn.getScriptParser());
						monitor.worked(1);
						counter++;
					}
				} catch (OdysseusScriptException ex) {
					LOG.error("Exception during executing script", ex);
					
					status = new Status(Status.ERROR, IEditorTextParserConstants.PLUGIN_ID, "Script Execution Error: " + ex.getRootMessage(), ex);
				}
				monitor.done();

				return status;
			}
		};
		job.setUser(true); 
		job.schedule(); 
	}

}
