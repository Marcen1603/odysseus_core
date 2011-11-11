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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.application.storing.controller.RecordingController;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class StartNewRecordingDialog extends TitleAreaDialog {
	private Text nameOfTheRecordingBox;
	private Text tableNameBox;
	private Combo comboDropDownStreams;
	private Combo comboDropDownDatabases;
	private String recordingName;
	private IDataDictionary dataDictionary;
	private User user;
	private String fromStream;
	private String databaseConnection;
	private String tableName;
	private Button sameAsRecording;
	private Button dropTableBefore;
	private Button truncateTableBefore;
	private Button appendTableButton;
	private boolean dropTable;
	private boolean truncateTable;
	private boolean appendTable;

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
		nameOfTheRecordingBox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				tableNameBox.setText(nameOfTheRecordingBox.getText());

			}
		});

		Label label2 = new Label(parent, SWT.NONE);
		label2.setText("From stream:");

		comboDropDownStreams = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		for (Entry<String, ILogicalOperator> e : this.dataDictionary.getStreamsAndViews(user)) {
			comboDropDownStreams.add(e.getKey());
		}

		Label label3 = new Label(parent, SWT.NONE);
		label3.setText("Use Database:");

		comboDropDownDatabases = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		for (String s : DatabaseConnectionDictionary.getInstance().getConnections().keySet()) {
			comboDropDownDatabases.add(s);
		}

		Label label4 = new Label(parent, SWT.NONE);
		label4.setText("Write into table:");

		sameAsRecording = new Button(parent, SWT.CHECK);
		sameAsRecording.setText("Same as recording Name");
		sameAsRecording.setSelection(true);
		sameAsRecording.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Button) {
					if (sameAsRecording.getSelection()) {
						tableNameBox.setEnabled(false);
						tableNameBox.setText(nameOfTheRecordingBox.getText());
					} else {
						tableNameBox.setEnabled(true);
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		new Label(parent, SWT.NONE);

		tableNameBox = new Text(parent, SWT.BORDER);
		tableNameBox.setLayoutData(gridData);
		tableNameBox.setEnabled(false);

		appendTableButton = new Button(parent, SWT.RADIO);
		appendTableButton.setText("Append to table");
		appendTableButton.setSelection(true);

		new Label(parent, SWT.NONE);

		dropTableBefore = new Button(parent, SWT.RADIO);
		dropTableBefore.setText("Drop the table before");

		truncateTableBefore = new Button(parent, SWT.RADIO);
		truncateTableBefore.setText("Truncate (clear) table before");

		return parent;
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private void saveInput() {
		this.recordingName = nameOfTheRecordingBox.getText();
		this.fromStream = comboDropDownStreams.getText();
		this.tableName = tableNameBox.getText();
		this.databaseConnection = comboDropDownDatabases.getText();
		if (this.sameAsRecording.getSelection()) {
			this.tableName = nameOfTheRecordingBox.getText();
		}
		this.dropTable = dropTableBefore.getSelection();
		this.truncateTable = truncateTableBefore.getSelection();
		this.appendTable = appendTableButton.getSelection();
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
		if (sameAsRecording.getSelection() && !validTableName(nameOfTheRecordingBox.getText())) {
			setErrorMessage("Table name may only consist of digits, letters and has to start with a letter!");
			valid = false;
		}
		if (!sameAsRecording.getSelection() && !validTableName(tableNameBox.getText())) {
			setErrorMessage("Table name may only consist of digits, letters and has to start with a letter!");
			valid = false;
		}
		if (comboDropDownDatabases.getSelectionIndex() == -1) {
			setErrorMessage("Please select a database connection");
			valid = false;
		}
		if (comboDropDownStreams.getSelectionIndex() == -1) {
			setErrorMessage("Please select a stream");
			valid = false;
		}
		if (RecordingController.getInstance().getRecords().keySet().contains(nameOfTheRecordingBox.getText())) {
			setErrorMessage("A recording with that name already exist!");
			valid = false;
		}
		if (comboDropDownDatabases.getSelectionIndex() != -1) {
			String db = comboDropDownDatabases.getItem(comboDropDownDatabases.getSelectionIndex());
			IDatabaseConnection con = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(db);
			String currentTableName = tableNameBox.getText();
			if (sameAsRecording.getSelection()) {
				currentTableName = nameOfTheRecordingBox.getText();
			}
			if (con.tableExists(currentTableName)) {
				MessageBox mbox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
				mbox.setText("Warning: There is already a table with that name!");
				String message = "";
				if(this.appendTableButton.getSelection()){
					message= "New data will be appended to the existing table!";
				}
				if(this.dropTableBefore.getSelection()){
					message= "The table is going to be dropped so that everything will be lost!";
					message= message+"\nThe table is dropped on each new recording!";
				}
				if(this.truncateTableBefore.getSelection()){
					message= "The table will be truncated so that everything will be lost!";
					message= message+"\nThe table is truncated on each new recording!";
				}
				mbox.setMessage(message+"\n\nProceed?"); 
				if (mbox.open() == SWT.NO) {
					setErrorMessage("Cancelled by user! Choose another option!");
					valid = false;
				}
				
			}
		}
		return valid;
	}

	private boolean validTableName(String name) {
		if (!Character.isLetter(name.charAt(0))) {
			return false;
		}
		for (char c : name.toCharArray()) {
			if (!Character.isDigit(c) && !Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public String getRecordingName() {
		return recordingName;
	}

	public String getFromStream() {
		return fromStream;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDatabaseConnection() {
		return databaseConnection;
	}

	public boolean isDropTable() {
		return dropTable;
	}

	public boolean isTruncateTable() {
		return truncateTable;
	}

	public boolean isAppendTable() {
		return appendTable;
	}

}
