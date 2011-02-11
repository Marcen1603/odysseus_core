package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Diese Klasse zeichnet den Editor für die Suche
 * 
 * @author Stefanie Witzke
 *
 */
public class SearchEditorPart extends EditorPart {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui.editorSearch";

	public SearchEditorPart() {
		// TODO Auto-generated constructor stub
	}

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
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		GridLayout gridLayout = new GridLayout(3, false);
		parent.setLayout(gridLayout);
		GridData gridData = new GridData();

		Label labelSearch = new Label(parent, SWT.NULL);
		labelSearch.setText("Value: ");

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;

		Text textfield = new Text(parent, SWT.SINGLE | SWT.BORDER);
		textfield.setLayoutData(gridData);

		Button buttonSearch = new Button(parent, SWT.PUSH);
		buttonSearch.setText("Search");
		buttonSearch.addSelectionListener(new SelectionAdapter() {
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
