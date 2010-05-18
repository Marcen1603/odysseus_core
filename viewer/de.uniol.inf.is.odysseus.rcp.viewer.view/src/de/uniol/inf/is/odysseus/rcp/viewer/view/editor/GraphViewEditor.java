package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class GraphViewEditor extends EditorPart {

	public static final String EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.GraphEditor";
	
	private GraphViewEditorInput input;
	
	public GraphViewEditor() {}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		this.input = ((GraphViewEditorInput)input);
		setPartName(this.input.getModelGraph().getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		Label label1 = new Label(parent, SWT.BORDER);
		label1.setText("Graph: ");
	}

	@Override
	public void setFocus() {
	}

}
