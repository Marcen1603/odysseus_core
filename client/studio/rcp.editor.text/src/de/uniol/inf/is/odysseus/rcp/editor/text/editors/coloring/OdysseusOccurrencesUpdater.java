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

package de.uniol.inf.is.odysseus.rcp.editor.text.editors.coloring;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;

/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class OdysseusOccurrencesUpdater implements ISelectionChangedListener {

	private final List<Annotation> oldAnnotations = new LinkedList<Annotation>();
	private OdysseusScriptEditor editor;

	public OdysseusOccurrencesUpdater(OdysseusScriptEditor editor) {
		this.editor = editor;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		update((ISourceViewer) event.getSource());
	}

	public void update(ISourceViewer viewer) {
		IDocument document = viewer.getDocument();
		IAnnotationModel model = viewer.getAnnotationModel();
		
		removeOldAnnotations(model);
		String word = getWordAtSelection(editor.getSelectionProvider().getSelection(), document);
		if (isValidWord(word)) {
			createNewAnnotations(word, document, model);
		}
	}

	private static String getWordAtSelection(ISelection selection, IDocument document) {
		if (selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			if (textSelection.getLength() > 0) {
				return textSelection.getText();
			}
            String wordStart = findStartOfWord(document, textSelection.getOffset());
            String wordEnd = findEndOfWord(document, textSelection.getOffset());
            return wordStart + wordEnd;
		}
		return "";
	}

	private static String findEndOfWord(IDocument doc, int offset) {
		try {
			for (int n = offset; n < doc.getLength(); n++) {
				char c = doc.getChar(n);
				if ((!Character.isJavaIdentifierPart(c)) || Character.isWhitespace(c)) {
					return doc.get(offset, n - offset);
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String findStartOfWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if ((!Character.isJavaIdentifierPart(c)) || Character.isWhitespace(c)) {
					return doc.get(n + 1, offset - n - 1);
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static boolean isValidWord(String word) {
		return !word.isEmpty();
	}

	private void removeOldAnnotations(IAnnotationModel model) {
		for (Iterator<Annotation> it = oldAnnotations.iterator(); it.hasNext();) {
			Annotation annotation = it.next();
			model.removeAnnotation(annotation);
		}
		oldAnnotations.clear();
	}

	private void createNewAnnotations(String ingredient, IDocument document, IAnnotationModel model) {
		if (model != null) {
			String content = document.get();
			int idx = content.indexOf(ingredient);
			while (idx != -1) {
				Annotation annotation = new Annotation(OdysseusRCPEditorTextPlugIn.ODYSSEUS_ANNOTATION_HIGHLIGHTING, false, ingredient);
				Position position = new Position(idx, ingredient.length());
				model.addAnnotation(annotation, position);
				oldAnnotations.add(annotation);
				idx = content.indexOf(ingredient, idx + 1);
			}
		}
	}

}
