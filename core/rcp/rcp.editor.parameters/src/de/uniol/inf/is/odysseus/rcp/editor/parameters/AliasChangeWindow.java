package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AliasChangeWindow {

	private Entry<String, String> alias;
	private Shell parent;
	private List<String> invalidNames;
	private TableViewer tableViewer;
	
	public AliasChangeWindow( Shell parent, Entry<String, String> alias, List<String> invalidNames, TableViewer tableViewer ) {
		this.parent = parent;
		this.alias = alias;
		this.invalidNames = invalidNames;
		this.tableViewer = tableViewer;
	}
	
	public void show() {
		createShell();
	}
	
	private void createShell() {
		final Shell wnd = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		wnd.setText("Change Attribute Alias");
		wnd.setSize(400, 140);
		
		GridLayout layout = new GridLayout();
		wnd.setLayout(layout);

		// Eingabebereich
		Composite inputComposite = new Composite(wnd, SWT.NONE);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = 2;
		inputComposite.setLayout(layout2);
		inputComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label originalNameLabel = new Label(inputComposite, SWT.NONE);
		originalNameLabel.setText("Original name");

		Text originalNameText = new Text(inputComposite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		originalNameText.setText(alias.getKey());
		originalNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		originalNameText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

		Label newNameLabel = new Label(inputComposite, SWT.NONE);
		newNameLabel.setText("New name");
		
		final Text nameText = new Text(inputComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.setText(alias.getValue()); 
		nameText.selectAll();
		nameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Enter-Taste
				if( e.keyCode == 13 ) {
					if( isTextOK(nameText.getText()))
						saveAndClose(nameText.getText(), wnd, tableViewer);
				}
			}
		});
		 		
		// Buttonbereich
		Composite buttonComposite = new Composite(wnd, SWT.NONE);
		GridLayout layout3 = new GridLayout();
		layout3.numColumns = 2;
		buttonComposite.setLayout(layout3);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Button okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setText("OK");
		okButton.setEnabled(false);
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveAndClose(nameText.getText(), wnd, tableViewer);
			}
		});
		// Wenn sich der Name ändert, schauen, ob OK-Button aktiv werden kann
		// (wenn der name gültig ist, kann OK angeklickt werden)
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				okButton.setEnabled(isTextOK(nameText.getText()));
			}
		});

		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wnd.dispose();
			}
		});
		
		wnd.setVisible(true);
		nameText.setFocus();
	}
	
	// speichert eingabe und schließt das fenster
	private void saveAndClose(String newName, Shell wnd, TableViewer viewer) {
		alias.setValue(newName);
		tableViewer.refresh();
		wnd.dispose();
	}

	// Prüft, ob der Name gültig ist
	protected boolean isTextOK(String txt) {
		if( txt == null || txt.length() == 0 ) 
			return false;
		
		if( invalidNames.contains(txt))
			return false;
		
		if( txt.contains("."))
			return false;
		
		return true;
	}
}
