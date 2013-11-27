package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class FilenameParameterPresentation extends StringParameterPresentation {

	@Override
	public Control createParameterWidget(final Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		String currentStr = "";
		if (getValue() != null) {
			currentStr = getValue().toString();
		}
		final Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text.setText(currentStr);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (text.getText().isEmpty()) {
					setValue(null);
				} else {
					setValue(text.getText());
				}
			}
		});
		Button button = new Button(container, SWT.PUSH);
		button.setText("... from filesystem");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN | SWT.SAVE);

				// Set the initial filter path according
				// to anything they've selected or typed in
				dlg.setFilterPath(text.getText());

				// Change the title bar text
				dlg.setText("Choose a file");
				String[] extensions = { "*.*" };
				dlg.setFilterExtensions(extensions);
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					text.setText(dir);
				}
			}
		});
		Button buttonProject = new Button(container, SWT.PUSH);
		buttonProject.setText("... from project");
		buttonProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(getProject());
				dialog.setAllowMultiple(false);
				if (dialog.open() == Window.OK) {
					IResource resource = (IResource) dialog.getFirstResult();					
					if (resource != null) {
						String dir = resource.getProjectRelativePath().toString();
						text.setText("${PROJECTPATH/}"+dir);
					}
				}	
			}
		});
		
		return container;
	}

}
