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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.coloring.OdysseusOccurrencesUpdater;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.outline.OdysseusScriptContentOutlinePage;

public class OdysseusScriptEditor extends AbstractDecoratedTextEditor implements
		IResourceChangeListener, ISaveablePart2 {

	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

	private OdysseusScriptContentOutlinePage outlinePage;
	private OdysseusOccurrencesUpdater occurrencesUpdater;

	public OdysseusScriptEditor() {
		super();
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });
		internal_init();
	}

	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new OdysseusScriptDocumentProvider());
		setSourceViewerConfiguration(new OdysseusScriptViewerConfiguration(this));
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.POST_CHANGE);

		this.occurrencesUpdater = new OdysseusOccurrencesUpdater(this);
		((IPostSelectionProvider) getSelectionProvider())
				.addPostSelectionChangedListener(this.occurrencesUpdater);
	}

	public void setModel() {
		if (this.occurrencesUpdater != null) {
			this.occurrencesUpdater.update(getSourceViewer());
		}
		if (getDocumentProvider() != null && getEditorInput() != null
				&& getDocumentProvider().getDocument(getEditorInput()) != null
				&& this.outlinePage != null) {
			this.outlinePage.setInput(getDocumentProvider().getDocument(
					getEditorInput()).get());
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new OdysseusScriptContentOutlinePage(
						getDocumentProvider().getDocument(getEditorInput())
								.get());
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	@Override
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		char[] matchChars = { '(', ')', '[', ']', '{', '}' };
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(
				matchChars);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(
				EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);
		IPreferenceStore store = getPreferenceStore();
		store.setDefault("matchingBrackets", true);
		store.setDefault("matchingBracketsColor", "128,128,128");
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (!(getEditorInput() instanceof FileEditorInput)) {
			return;
		}
		IFile file = ((FileEditorInput) getEditorInput()).getFile();
		IResourceDelta delta = event.getDelta().findMember(file.getFullPath());
		if (delta != null) {
			// this.close(false);
		}

	}

	@Override
	public int promptToSaveOnClose() {
		String[] buttons = new String[]{IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL};
		MessageDialog dialog = new MessageDialog(getEditorSite().getShell(), "Save changes", null,  "Save the changes?", MessageDialog.QUESTION,buttons,0);
		final int dialogResult = dialog.open();
		if(dialogResult == 0){
			return ISaveablePart2.YES;
		}else if(dialogResult == 1){
			return ISaveablePart2.NO;
		}
		return ISaveablePart2.CANCEL;
	}
}
