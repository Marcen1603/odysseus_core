/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.application.storing.view.dialogs;

import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class StartNewRecordingDialog extends TitleAreaDialog {
	private Text nameOfTheRecordingBox;
	private Combo comboDropDownStreams;
	private Combo comboDropDownSinks;
	private Combo comboDropDownDatabases;
	private String input;
	private IDataDictionary dataDictionary;
	private User user;
	private String fromStream;
	private String toStream;
	private String databaseConnection;

	

	public StartNewRecordingDialog(Shell parentShell, IDataDictionary dd, User user) {
		super(parentShell);
		this.dataDictionary = dd;		
		this.user = user;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Create Recording");
		setMessage("Creates a new recording.", IMessageProvider.NONE);

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Recording name:");

		
		nameOfTheRecordingBox = new Text(parent, SWT.BORDER);
		nameOfTheRecordingBox.setLayoutData(gridData);
		

		Label label2 = new Label(parent, SWT.NONE);
		label2.setText("From stream:");
		
		comboDropDownStreams = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		for(Entry<String, ILogicalOperator> e : this.dataDictionary.getStreamsAndViews(user)){
			comboDropDownStreams.add(e.getKey());
		}
		
		Label label3 = new Label(parent, SWT.NONE);
		label3.setText("To sink:");
		
		comboDropDownSinks = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);		
		for(Entry<String, ILogicalOperator> e : this.dataDictionary.getSinks(user)){
			comboDropDownSinks.add(e.getKey());
		}
		
		
		Label label4 = new Label(parent, SWT.NONE);
		label4.setText("Use Database:");
		
		comboDropDownDatabases = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		for(String s : DatabaseConnectionDictionary.getInstance().getConnections().keySet()){
			comboDropDownDatabases.add(s);
		}

		return parent;
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private void saveInput() {
		this.input = nameOfTheRecordingBox.getText();
		this.fromStream = comboDropDownStreams.getText();
		this.toStream = comboDropDownSinks.getText();
		this.databaseConnection = comboDropDownDatabases.getText();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Ok", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label, boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;
		if (nameOfTheRecordingBox.getText().length() == 0) {
			setErrorMessage("Please maintain the name");
			valid = false;
		}
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public String getFromStream() {
		return fromStream;
	}

	public void setFromStream(String fromStream) {
		this.fromStream = fromStream;
	}

	public String getToStream() {
		return toStream;
	}

	public void setToStream(String toStream) {
		this.toStream = toStream;
	}

	public String getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(String databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

}
