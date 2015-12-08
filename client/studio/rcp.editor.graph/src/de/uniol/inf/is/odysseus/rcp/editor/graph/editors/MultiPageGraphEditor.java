package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
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
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator.ScriptGenerator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.views.OperatorGraphPropertyView;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageGraphEditor extends MultiPageEditorPart implements IResourceChangeListener, ISaveablePart2 {

	
	

	private StyledText text;

	private int editorIndex;

	private int textIndex;

	private TextEditor editor;

	private OperatorGraphEditor graphEditor;

	private boolean dirty = false;
	
	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageGraphEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates page 2: this is a text-representation
	 */
	void createPage2() {
		text = new StyledText(getContainer(), SWT.READ_ONLY);
		textIndex = addPage(text);
		setPageText(textIndex, "PQL");
	}

	/**
	 * the real file with all data including positions etc.
	 */
	void createPage3() {
		try {
			editor = new TextEditor();
			editorIndex = addPage(editor, getEditorInput());
			setTitlesFromFileName(editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1: some properties
	 */
	void createPage1() {				
		Composite composite = new Composite(getContainer(), SWT.NONE);			
		composite.setLayoutData(GridData.FILL_BOTH);
		GridLayout layout = new GridLayout(1, false);		
		composite.setLayout(layout);		
		int index = addPage(composite);
		setPageText(index, "Properties");
		
		Label l = new Label(composite, SWT.NONE);
		l.setText("Heading");
		GridData dt = new GridData(GridData.FILL, GridData.FILL, true, true);
		final Text text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setText(getGraphEditor().getHeading());
		text.setLayoutData(dt);
		
		
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				setHeading(text.getText());
				setDirty(true);
			}
		});
	}

	/**
	 * Creates the page for the graphical GEF editor
	 */
	void createPage0() {
		try {
			graphEditor = new OperatorGraphEditor();
			int graphEditorIndex = addPage(graphEditor, getEditorInput());
			setPageText(graphEditorIndex, "Graph");
			graphEditor.getGraph().addObserver(new Observer() {

				@Override
				public void update(Observable arg0, Object arg1) {
					modelChanged();
				}
			});			
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested graph editor", null, e.getStatus());
		}
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	@Override
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();
	}

	@Override
	public void setFocus() {
		graphEditor.updateGraph();
	}
	
	private void modelChanged() {
		reloadTextEditor();
		setDirty(true);
		graphEditor.updateGraph();
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		informGraphOperatorPropertyView();
		super.dispose();
	}

	/**
	 * 
	 */
	private void informGraphOperatorPropertyView() {
		for (IWorkbenchPage page : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()) {
			for (IViewReference ref : page.getViewReferences()) {
				IWorkbenchPart part = ref.getPart(false);
				if (part instanceof OperatorGraphPropertyView) {
					OperatorGraphPropertyView view = (OperatorGraphPropertyView) part;
					view.opeartorGraphEditorClosed();
				}
			}
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {		
		getGraphEditor().doSave(monitor);
		setDirty(false);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(ISaveablePart.PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {

		if (isDirty()) {
			int result = promptToSaveOnClose();
			if (result == ISaveablePart2.CANCEL) {
				return;
			}
			if (result == ISaveablePart2.YES) {
				doSave(new NullProgressMonitor());
			}
			try {
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		IFile oldFile = ((FileEditorInput) getEditorInput()).getFile();
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalName(oldFile.getName());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {

				try {
					file.create(oldFile.getContents(), true, new NullProgressMonitor());
					setInputWithNotify(new FileEditorInput(file));
					setTitlesFromFileName(file.getName());
					doSave(getProgressMonitor());
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void setTitlesFromFileName(String name) {
		setPartName("Operator Graph Editor (" + name + ")");
		setPageText(3, name);
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

	/**
	 * @return
	 */
	private IProgressMonitor getProgressMonitor() {
		IProgressMonitor pm = null;
		if (getEditorSite() != null && getEditorSite().getActionBars() != null) {
			IStatusLineManager manager = getEditorSite().getActionBars().getStatusLineManager();
			if (manager != null)
				pm = manager.getProgressMonitor();
		}
		return pm != null ? pm : new NullProgressMonitor();
	}

	protected void doSaveAs(IEditorInput editorInput) {
		setInputWithNotify(editorInput);
		setPageText(3, editorInput.getName());
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		doSave(progressMonitor);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// if (adapter == CommandStack.class) {
		// return graphEditor.getCurrentEditDomain().getCommandStack();
		// }
		Object ob = graphEditor.getAdapter(adapter);
		if (ob != null) {
			return ob;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Closes all project files on project close.
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_DELETE || event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			closeEditor(event.getResource());
		}
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			try {
				IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
					@Override
					public boolean visit(IResourceDelta delta) {
						if (delta.getFlags() != IResourceDelta.MARKERS && delta.getResource().getType() == IResource.FILE) {
							if (delta.getKind() == IResourceDelta.REMOVED) {
								IResource resource = delta.getResource();
								if (resource.equals(((FileEditorInput) getEditorInput()).getFile())) {
									closeEditor(resource);
								}
							}
							// TODO: handle IResourceDelta.CHANGED events by
							// offering
							// to reload the file
						}

						return true;
					}
				};

				delta.accept(visitor);
			} catch (CoreException exception) {
				exception.printStackTrace();
			}
		}
		// if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
		//
		// IResourceDelta delta = event.getDelta();
		// try {
		// IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
		// public boolean visit(IResourceDelta delta) {
		// if (delta.getFlags() != IResourceDelta.MARKERS &&
		// delta.getResource().getType() == IResource.FILE) {
		// if (delta.getKind() == IResourceDelta.REMOVED) {
		// IResource resource = delta.getResource();
		// }
		// }
		// }
		// }
		// }
		//
		// if (event.getDelta().getKind() == IResourceDelta.REMOVED) {
		// if (event.getDelta().getResource().equals(((FileEditorInput)
		// getEditorInput()).getFile())) {
		// closeEditor(event.getDelta().getResource());
		// }
		// }
		// }
	}

	/**
	 * 
	 */
	private void closeEditor(final IResource resource) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i < pages.length; i++) {
					if (((FileEditorInput) editor.getEditorInput()).getFile().equals(resource)) {
						IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
						pages[i].closeEditor(editorPart, true);
					}
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == textIndex) {
			reloadTextEditor();
		}
		if (newPageIndex == editorIndex) {
			reloadFileEditor();
		}
	}

	private void reloadFileEditor() {

	}

	private void reloadTextEditor() {
		String pql = "There are still problems in your graph, so that PQL cannot be generated!";
		try {
			pql = this.graphEditor.createFullHeading();
			pql = pql+getPQLString();
		} catch (Exception e) {

		}
		text.setText(pql);

	}

	private String getPQLString() {
		return ScriptGenerator.buildPQL(graphEditor.getGraph());
	}

	/**
	 * @return
	 */
	public OperatorGraphEditor getGraphEditor() {
		return this.graphEditor;
	}

	public void executeScript() {
		IFile file = ((FileEditorInput) getEditorInput()).getFile();
		String pql = this.graphEditor.createFullHeading();
		pql = pql+getPQLString();
		Activator.getDefault().getExecutor().addQuery(pql, "OdysseusScript", Activator.getDefault().getCaller(), ParserClientUtil.createRCPContext(file));
	}
	
	
	

	

	public void setHeading(String heading) {
		this.getGraphEditor().setHeading(heading);
	}

}
