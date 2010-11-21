package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;


public class FilePathParameterEditor extends StringParameterEditor {


	@Override
	protected Control createInputControl(Composite parent) {
		Composite comp = new Composite( parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		
		// Input-SWT holen
		final Text ctrl = (Text)super.createInputControl(comp);
		ctrl.setLayoutData( new GridData( GridData.FILL_HORIZONTAL));
		
		// Button zur Fileauswahl
		Button fileSelectAuswahl = new Button( comp, SWT.PUSH );
		fileSelectAuswahl.setText("Browse...");
		fileSelectAuswahl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
				String selectedFile = dlg.open();
				if( selectedFile != null ) {
//					setValue(selectedFile);
					ctrl.setText(selectedFile);
				}
			}
		});
		
		return comp;
	}
}
