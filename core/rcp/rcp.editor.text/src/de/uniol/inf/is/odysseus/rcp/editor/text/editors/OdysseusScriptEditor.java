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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.coloring.OdysseusOccurrencesUpdater;

public class OdysseusScriptEditor extends AbstractDecoratedTextEditor implements ISaveablePart2, IResourceChangeListener, IResourceDeltaVisitor {

	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

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
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		this.occurrencesUpdater = new OdysseusOccurrencesUpdater(this);
		((IPostSelectionProvider) getSelectionProvider()).addPostSelectionChangedListener(this.occurrencesUpdater);
	}

	public void setModel() {
		if (this.occurrencesUpdater != null) {
			ISourceViewer viewer = getSourceViewer();
			if (viewer != null) {
				this.occurrencesUpdater.update(viewer);
			}
		}
	}

	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		char[] matchChars = { '(', ')', '[', ']', '{', '}' };
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(matchChars);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);
		IPreferenceStore store = getPreferenceStore();
		store.setDefault("matchingBrackets", true);
		store.setDefault("matchingBracketsColor", "128,128,128");
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	public int promptToSaveOnClose() {
		String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL };
		MessageDialog dialog = new MessageDialog(getEditorSite().getShell(), "Save changes", null, "Save the changes?", MessageDialog.QUESTION, buttons, 0);
		final int dialogResult = dialog.open();
		if (dialogResult == 0) {
			return ISaveablePart2.YES;
		} else if (dialogResult == 1) {
			return ISaveablePart2.NO;
		}
		return ISaveablePart2.CANCEL;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			try {
				delta.accept(this);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			closeEditor(event.getResource());
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.equals(((IFileEditorInput) getEditorInput()).getFile())) {
				switch (delta.getKind()) {
				case IResourceDelta.REMOVED:
				case IResourceDelta.REPLACED:
					closeEditor(delta.getResource());
					break;
				default:
					break;
				}
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		if(isDirty()){
			int result = promptToSaveOnClose();
			if(result == ISaveablePart2.CANCEL){
				return;
			}
			if(result == ISaveablePart2.YES){
				doSave(new NullProgressMonitor());
			}
			try {
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			} catch (CoreException e) {			
				e.printStackTrace();
			}
		}
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalName(getTitle());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				IFile oldFile = ((FileEditorInput) getEditorInput()).getFile();
				try {					
					file.create(oldFile.getContents(), true, new NullProgressMonitor());
					setInputWithNotify(new FileEditorInput(file));
					setPartName(file.getName());
					doSave(getProgressMonitor());
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void closeEditor(final IResource resource) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i < pages.length; i++) {
					if (((FileEditorInput) getEditorInput()).getFile().equals(resource)) {
						IEditorPart editorPart = pages[i].findEditor(getEditorInput());
						pages[i].closeEditor(editorPart, true);
					}
				}
			}
		});
	}

}
