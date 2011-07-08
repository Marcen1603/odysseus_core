/** Copyright 2011 The Odysseus Team
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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptDocumentProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;

/**
 * 
 * @author Dennis Geesen Created at: 07.07.2011
 */
public class CommentTextCommand extends AbstractHandler implements IHandler {

	private static final String COMMENT_STRING = "///";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();

			if (obj instanceof IFile) {

				return null;
			}
		}

		// Check if we have an active Editor
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if (part instanceof OdysseusScriptEditor) {
			OdysseusScriptEditor editor = (OdysseusScriptEditor) part;			
			ISelectionProvider selectionProvider = editor.getSelectionProvider();			
			TextSelection sel = (TextSelection) selectionProvider.getSelection();			
			OdysseusScriptDocumentProvider docPro = (OdysseusScriptDocumentProvider) editor.getDocumentProvider();
			Document doc = (Document) docPro.getDocument(editor.getEditorInput());
			String text = doc.get();
			String lines[] = text.split(doc.getDefaultLineDelimiter());
			String toWrite = "";
			String delimiter = "";

			// wenn alle kommentiert sind, dann alles auskommentieren
			if (allLinesCommented(lines, sel.getStartLine(), sel.getEndLine())) {
				for (int linenumber = 0; linenumber < lines.length; linenumber++) {
					if (linenumber >= sel.getStartLine() && linenumber <= sel.getEndLine()) {
						lines[linenumber] = lines[linenumber].substring(3);
					}
					toWrite = toWrite + delimiter + lines[linenumber];
					delimiter = doc.getDefaultLineDelimiter();
				}
			} else {
				// wenn nur teile kommentiert sind oder gar nichts, dann alles kommentieren				
					for (int linenumber = 0; linenumber < lines.length; linenumber++) {
						if (linenumber >= sel.getStartLine() && linenumber <= sel.getEndLine()) {
							lines[linenumber] = COMMENT_STRING + lines[linenumber];
						}
						toWrite = toWrite + delimiter + lines[linenumber];
						delimiter = doc.getDefaultLineDelimiter();
					}				
			}			
			doc.set(toWrite);
			editor.getSelectionProvider().setSelection(sel);
			return null;
		}

		return null;
	}	
	
	private boolean allLinesCommented(String[] lines, int from, int to) {
		for (int linenumber = 0; linenumber < lines.length; linenumber++) {
			if (linenumber >= from && linenumber <= to) {
				if (!lines[linenumber].startsWith(COMMENT_STRING)) {
					return false;
				}
			}
		}
		return true;
	}

}
