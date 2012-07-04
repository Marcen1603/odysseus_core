package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class DashboardPartEditor extends EditorPart {

	public DashboardPartEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName("DashboardPart");
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
		parent.setLayout(new GridLayout());
		
		Label label = new Label(parent, SWT.BORDER);
		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		label.setText("Odysseus Dashboard Part Editor");
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		
		
	}

	@Override
	public void setFocus() {

	}

}
