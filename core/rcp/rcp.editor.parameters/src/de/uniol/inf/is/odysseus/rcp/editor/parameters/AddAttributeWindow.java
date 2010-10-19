package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class AddAttributeWindow {

	private static final String NAME_TEXT = "Complete name";
	private static final String TYPE_TEXT = "Datatype";
	private static final String TITLE_TEXT = "Create new attribute";
	private static final String OK_TEXT = "OK";
	private static final String CANCEL_TEXT = "Cancel";

	private Shell parent;
	private SDFAttribute attribute = null;
	private List<String> restrictedNames = null;
	private Button okButton;
	private Text nameText;

	public AddAttributeWindow(Shell parent, List<String> restrictedNames) {
		setParent(parent);
		setRestrictedNames(restrictedNames);
	}

	public void show(DisposeListener disposeListener) {
		Shell wnd = createWindow();
		wnd.addDisposeListener(disposeListener);
		wnd.setVisible(true);
		nameText.setFocus();
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	private Shell createWindow() {

		// Fenster
		final Shell wnd = new Shell(getParent(), SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM);
		wnd.setText(TITLE_TEXT);
		wnd.setSize(400, 140);
		GridLayout layout = new GridLayout();
		wnd.setLayout(layout);

		// Eingabebereich
		Composite inputComposite = new Composite(wnd, SWT.NONE);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = 2;
		inputComposite.setLayout(layout2);
		inputComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label nameLabel = new Label(inputComposite, SWT.NONE);
		nameLabel.setText(NAME_TEXT);

		nameText = new Text(inputComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateOkButtonEnabled();
			}

		});

		Label typeLabel = new Label(inputComposite, SWT.NONE);
		typeLabel.setText(TYPE_TEXT);

		final Combo typeComboBox = new Combo(inputComposite, SWT.READ_ONLY
				| SWT.BORDER);
		typeComboBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		typeComboBox.add("Integer");
		typeComboBox.add("Long");
		typeComboBox.add("Double");
		typeComboBox.add("String");
		typeComboBox.add("Date");
		typeComboBox.add("StartTimestamp");
		typeComboBox.add("EndTimestamp");
		typeComboBox.select(0); // ersten eintrag wählen

		// Buttonbereich
		Composite buttonComposite = new Composite(wnd, SWT.NONE);
		GridLayout layout3 = new GridLayout();
		layout3.numColumns = 2;
		buttonComposite.setLayout(layout3);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setText(OK_TEXT);
		okButton.setEnabled(false);
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				attribute = new SDFAttribute(nameText.getText());
				attribute.setDatatype(SDFDatatypeFactory
						.getDatatype(typeComboBox.getItem(typeComboBox
								.getSelectionIndex())));
				wnd.dispose();
			}
		});

		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(CANCEL_TEXT);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				attribute = null;
				wnd.dispose();
			}
		});

		return wnd;
	}

	public Shell getParent() {
		return parent;
	}

	private void setParent(Shell parent) {
		if (parent == null)
			throw new IllegalArgumentException("parent is null");

		this.parent = parent;
	}

	private List<String> getRestrictedNames() {
		return restrictedNames;
	}

	private void setRestrictedNames(List<String> restrictedNames) {
		if (parent == restrictedNames)
			throw new IllegalArgumentException("restrictedNames is null");

		this.restrictedNames = restrictedNames;
	}

	private void updateOkButtonEnabled() {
		if (nameText.getText() == null || nameText.getText().length() == 0) {
			okButton.setEnabled(false);
		} else if (getRestrictedNames().contains(nameText.getText())) {
			okButton.setEnabled(false);
		} else if (!nameText.getText().contains(".")) {
			okButton.setEnabled(false);
		} else {
			okButton.setEnabled(true);
		}
	}

}
