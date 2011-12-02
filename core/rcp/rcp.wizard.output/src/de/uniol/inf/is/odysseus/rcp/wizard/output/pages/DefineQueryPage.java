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

package de.uniol.inf.is.odysseus.rcp.wizard.output.pages;

import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.OutputWizard;

/**
 * 
 * @author Dennis Geesen Created at: 01.12.2011
 */
public class DefineQueryPage extends AbstractWizardPage<OutputWizard> {

	private Button openDialogButton;
	private String path;
	private String selectedFile = null;
	private Text textInput;
	private StyledText textExample;

	public DefineQueryPage(String title, String path) {
		super(title);
		setTitle(title);
		setMessage("Please define the query");
		this.path = path;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		textInput = new Text(composite, SWT.SEARCH);
		textInput.setLayoutData(gridData);

		openDialogButton = new Button(composite, SWT.PUSH);
		openDialogButton.setText("Select query file");

		openDialogButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterPath(path);
				fileDialog.setFilterExtensions(new String[] { "*.qry" });
				fileDialog.setFilterNames(new String[] { "Odysseus Query Files (*.qry)" });
				fileDialog.setText("FileDialog");
				selectedFile = fileDialog.open();
				textInput.setText(selectedFile);
				getWizard().getOutputModel().setQueryFile(selectedFile);
				if (!setText()) {
					setErrorMessage("File not found! Cannot read from file!");
				} else {
					performNext();
					setPageComplete(true);
				}
			}
		});

		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 2;
		gridData2.horizontalAlignment = SWT.FILL;
		gridData2.verticalAlignment = SWT.FILL;
		gridData2.grabExcessVerticalSpace = true;

		textExample = new StyledText(composite, SWT.BORDER);
		textExample.setLayoutData(gridData2);

		setControl(composite);
		setPageComplete(false);
	}

	@Override
	public boolean canFinish() {
		return false;
	}

	@Override
	public void performNext() {
		getWizard().getOutputModel().setQueryFile(selectedFile);
	}

	@Override
	public boolean canFlipToNextPage() {
		if (this.selectedFile != null && !this.selectedFile.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean setText() {
		this.textExample.setText("");
		try {
			this.textExample.setText(getWizard().getOutputModel().getQueryText());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

}
