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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.StringEditorInput;
import de.uniol.inf.is.odysseus.rcp.editor.script.model.VisualOdysseusScript;
import de.uniol.inf.is.odysseus.rcp.editor.script.model.VisualOdysseusScriptModel;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.coloring.OdysseusOccurrencesUpdater;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class OdysseusScriptEditor extends AbstractDecoratedTextEditor implements ISaveablePart2, IResourceChangeListener, IResourceDeltaVisitor  {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusScriptEditor.class);
	
	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

	private OdysseusOccurrencesUpdater occurrencesUpdater;
	
	private VisualOdysseusScript visualScript;
	private boolean visualChanged = false;

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
		IEditorInput input = getEditorInput();
		if( input instanceof IFileEditorInput && ((IFileEditorInput)input).getFile().getFileExtension().equals("qry")) {
			CTabFolder tabFolder = new CTabFolder(parent, SWT.BOTTOM);
			tabFolder.setLayout(new GridLayout());
			tabFolder.setBorderVisible(true);
			
			CTabItem textTab = new CTabItem(tabFolder, SWT.DOUBLE_BUFFERED);
			textTab.setText("Text");
			
			Composite textComposite = new Composite(tabFolder, SWT.NONE);
			textComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			textComposite.setLayout(new FillLayout());
			super.createPartControl(textComposite);
			textTab.setControl(textComposite);
			
			CTabItem visualTab = new CTabItem(tabFolder, SWT.NONE);
			visualTab.setText("Visual");
			
			Composite visualComposite = new Composite(tabFolder, SWT.DOUBLE_BUFFERED);
			visualComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			visualComposite.setLayout(new FillLayout());
			
			visualTab.setControl(visualComposite);
	
			tabFolder.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if( tabFolder.getSelectionIndex() == 0 ) {
						// text selected
						writeVisualScriptToDocumentIfNeeded();
	
						if( visualScript != null ) {
							visualScript.dispose();
							visualScript = null;
						}
						
					} else {
						// visual selected
						if( visualScript != null ) {
							return;
						}
						
						Optional<VisualOdysseusScriptModel> optModel = createVisualOdysseusScriptModel();
						if( optModel.isPresent() ) {
							for( Control ctrl : visualComposite.getChildren() ) {
								ctrl.dispose();
							}
							
							visualScript = createVisualScript(visualComposite, optModel.get());
							visualComposite.layout();
						} else {
							Label errorLabel = new Label(visualComposite, SWT.NONE);
							errorLabel.setText("Could not generate visual odysseus script from current text");
						}
					}
				}
	
			});
			
			tabFolder.setSelection(0);
		} else {
			// for pql and cql editors
			super.createPartControl(parent);
		}
		
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
	
	private Optional<VisualOdysseusScriptModel> createVisualOdysseusScriptModel() {
		try {
			IDocument document = getDocumentProvider().getDocument(getEditorInput());
			String editorContents = document.get();
			
			VisualOdysseusScriptModel scriptModel = new VisualOdysseusScriptModel();
			scriptModel.parse(editorContents);
			
			return Optional.of(scriptModel);
			
		} catch (VisualOdysseusScriptException e) {
			LOG.error("Could not read contents of file", e);
			return Optional.absent();
		} 
	}
	
	private VisualOdysseusScript createVisualScript(Composite visualComposite, VisualOdysseusScriptModel scriptModel) {
		ScrolledComposite scrollComposite = new ScrolledComposite(visualComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);

		Composite contentComposite = new Composite(scrollComposite, SWT.NONE);
		GridLayout contentLayout = new GridLayout();
		contentLayout.verticalSpacing = 0;
		contentLayout.marginHeight = 0;
		contentLayout.marginWidth = 0;
		contentComposite.setLayout(contentLayout);

		scrollComposite.setContent(contentComposite);

		VisualOdysseusScript visualScript = new VisualOdysseusScript(contentComposite, scriptModel, new IVisualOdysseusScriptContainer() {
			
			@Override
			public void setTitleText(String title) {
				// do nothing atm
			}
			
			@Override
			public void setDirty(boolean dirty) {
				visualChanged = dirty;
				OdysseusScriptEditor.this.firePropertyChange(IEditorPart.PROP_DIRTY);
			}
			
			@Override
			public void layoutAll() {
				visualComposite.layout();
				
				scrollComposite.layout();
				contentComposite.layout();
				
				scrollComposite.setMinSize(contentComposite.getSize());
			}
			
			@Override
			public IFile getFile() {
				return ((IFileEditorInput)OdysseusScriptEditor.this.getEditorInput()).getFile();
			}
		});
		scrollComposite.setMinSize(scrollComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		return visualScript;
	}
	
	@Override
	public boolean isDirty() {
		return super.isDirty() || visualChanged;
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
		
		if( getEditorInput() instanceof StringEditorInput ) {
			((StringEditorInput)getEditorInput()).dispose();
		}
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
			if (file.equals(getFile())) {
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
	
	public final IFile getFile() {
		IEditorInput input = getEditorInput();
		if( input instanceof IFileEditorInput ) {
			return ((IFileEditorInput) getEditorInput()).getFile();
		} else if( input instanceof IStorageEditorInput) {
			return ((IStorageEditorInput)input).getAdapter(IFile.class);
		}
		
		return null;
	}
	
	public final String getUsedParser() {
		IEditorInput input = getEditorInput();
		if( input instanceof IFileEditorInput ) {
			return ((IFileEditorInput) getEditorInput()).getFile().getFileExtension();
		} else if( input instanceof StringEditorInput) {
			return ((StringEditorInput)input).getExtension();
		}
		
		return null;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

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
		
		if( getEditorInput() instanceof IFileEditorInput ) {
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
		} else if( getEditorInput() instanceof IStorageEditorInput ){
			// TODO
		}
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		writeVisualScriptToDocumentIfNeeded();			
		
		super.doSave(progressMonitor);
	}

	private void writeVisualScriptToDocumentIfNeeded() {
		if( visualScript != null && visualChanged ) {
			try {
				String newScript = visualScript.generateOdysseusScript();
				IDocument document = getDocumentProvider().getDocument(getEditorInput());
				document.set(newScript);
				visualChanged = false;
				
			} catch (VisualOdysseusScriptException e1) {
				new ExceptionWindow("Could not generate odysseus script", e1);
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
