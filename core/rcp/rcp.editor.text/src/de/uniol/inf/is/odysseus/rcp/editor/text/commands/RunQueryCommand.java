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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptDocumentProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class RunQueryCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RunQueryCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<IFile> optFileToRun = getSelectedFiles();
		if (!optFileToRun.isEmpty()) {
			runFiles(optFileToRun);

		} else {

			Optional<OdysseusScriptEditor> optEditor = getScriptEditor();
			if (optEditor.isPresent()) {
				OdysseusScriptEditor editor = optEditor.get();
				String[] lines = readLinesFromEditor(editor);
				execute(lines);
				return null;
			}
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return !getSelectedFiles().isEmpty() || getScriptEditor().isPresent();
	}

	@SuppressWarnings("unchecked")
	private static List<IFile> getSelectedFiles() {
		List<IFile> foundFiles = Lists.newArrayList();
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			List<Object> selectedObjects = ((IStructuredSelection) selection).toList();

			for (Object obj : selectedObjects) {
				if (isQueryTextFile(obj)) {
					foundFiles.add((IFile) obj);
				}
			}
		}

		return foundFiles;
	}

	private static boolean isQueryTextFile(Object obj) {
		return obj instanceof IFile && ((IFile) obj).getFileExtension().equals(OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION);
	}

	private static Optional<OdysseusScriptEditor> getScriptEditor() {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof OdysseusScriptEditor) {
			return Optional.of((OdysseusScriptEditor) part);
		}
		return Optional.absent();
	}

	private static String[] readLinesFromEditor(OdysseusScriptEditor editor) {
		OdysseusScriptDocumentProvider docPro = (OdysseusScriptDocumentProvider) editor.getDocumentProvider();
		Document doc = (Document) docPro.getDocument(editor.getEditorInput());
		String text = doc.get();
		String lines[] = text.split(doc.getDefaultLineDelimiter());
		return lines;
	}

	private void runFiles(List<IFile> optFileToRun) {
		try {
			List<String> result = Lists.newArrayList();
			for (IFile file : optFileToRun) {
				result.addAll(readLinesFromFile(file));
			}
			
			execute(result.toArray(new String[result.size()]));
		} catch (Exception ex) {
			LOG.error("Exception during running query file", ex);
			new ExceptionWindow(ex);
		}

	}

	private static List<String> readLinesFromFile(IFile queryFile) throws CoreException, IOException {
		// Datei öffnen
		if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
			queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
		}

		// Datei einlesen
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(queryFile.getContents()));
		String line = br.readLine();
		while (line != null) {
			lines.add(line);
			line = br.readLine();
		}
		br.close();
		return lines;
	}

	private static void execute(final String[] text) {
		Job job = new Job("Parsing and Executing Query") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
				try {
					executor.addQuery(concatLines(text), "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), "Standard");
				} catch( Throwable ex ) {
					Throwable cause = ex;
					// Exceptions are wrapped ... try to find the real cause :-)
					while ((cause instanceof OdysseusScriptException || cause instanceof QueryParseException) && cause.getCause() != null){
						cause = cause.getCause();
					}
					
					return new Status(Status.ERROR, IEditorTextParserConstants.PLUGIN_ID, "Script Execution Error", cause);
				}
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}
	

	private static String concatLines(String[] text) {
		StringBuilder sb = new StringBuilder();
		for( String t : text ) {
			sb.append(t).append("\n");
		}
		return sb.toString();
	}

}
