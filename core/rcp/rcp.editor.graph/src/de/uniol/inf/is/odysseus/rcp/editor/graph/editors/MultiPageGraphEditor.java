package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.HashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator.ScriptGenerator;

/**
 * An example showing how to create a multi-page editor. This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageGraphEditor extends MultiPageEditorPart implements IResourceChangeListener {

	/** The text editor used in page 0. */
	private StyledText text;
	
	private int editorIndex;

	private int textIndex;

	private TextEditor editor;

	private OperatorGraphEditor graphEditor;

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
		text = new StyledText(getContainer(), SWT.None);
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
			setPageText(editorIndex, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1: some properties
	 */
	void createPage1() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;

		int index = addPage(composite);
		setPageText(index, "Properties");
	}

	/**
	 * Creates the page for the graphical GEF editor
	 */
	void createPage0() {
		// Composite parent = new Composite(getContainer(), SWT.NONE);
		// FillLayout layout = new FillLayout();
		// parent.setLayout(layout);
		//
		// SashForm form = new SashForm(parent, SWT.HORIZONTAL);
		// createPaletteViewer(form);
		// createGraphViewer(form);
		// form.setWeights(new int[] { 15, 85 });
		//
		// int index = addPage(parent);
		// setPageText(index, "Graph");
		try {
			graphEditor = new OperatorGraphEditor();
			int graphEditorIndex = addPage(graphEditor, getEditorInput());
			setPageText(graphEditorIndex, "Graph");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested graph editor", null, e.getStatus());
		}
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all nested editors. Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this multi-page editor's input to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == CommandStack.class) {
			return graphEditor.getCurrentEditDomain().getCommandStack();
		}
		return super.getAdapter(adapter);
	}

	/**
	 * 
	 */

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
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
		ScriptGenerator generator = new ScriptGenerator(graphEditor.getGraph(), new HashMap<String, String>());
		String pql = generator.buildPQL();
		text.setText(pql);

	}

	/**
	 * @return
	 */
	public OperatorGraphEditor getGraphEditor() {
		return this.graphEditor;
	}
	
	
}
