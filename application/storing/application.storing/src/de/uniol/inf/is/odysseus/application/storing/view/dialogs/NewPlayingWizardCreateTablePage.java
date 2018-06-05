/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;

/**
 * 
 * @author Dennis Geesen Created at: 29.11.2011
 */
public class NewPlayingWizardCreateTablePage extends AbstractWizardPage<NewPlayingWizard>  {

	private Button createTable10Button;
	private Button createTable20Button;
	private Button createTable50Button;
	private Button createTableAllButton;
	private Button createDumpButton;

	protected NewPlayingWizardCreateTablePage() {		
		setTitle("New Playback");
		setMessage("Please choose table type");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		// create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 1;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		createTable10Button = new Button(composite, SWT.RADIO);
		createTable10Button.setText("Create a table with 10 lines");
		createTable10Button.addListener(SWT.Selection, this);

		createTable20Button = new Button(composite, SWT.RADIO);
		createTable20Button.setText("Create a table with 20 lines");
		createTable20Button.addListener(SWT.Selection, this);

		createTable50Button = new Button(composite, SWT.RADIO);
		createTable50Button.setText("Create a table with 50 lines");
		createTable50Button.addListener(SWT.Selection, this);

		createTableAllButton = new Button(composite, SWT.RADIO);
		createTableAllButton.setText("Create a table with unlimited lines");
		createTableAllButton.addListener(SWT.Selection, this);

		createDumpButton = new Button(composite, SWT.RADIO);
		createDumpButton.setText("Show only raw dump");
		createDumpButton.addListener(SWT.Selection, this);

		setControl(composite);
		setPageComplete(false);

	}

	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public boolean canFinish() {
		return createTable10Button.getSelection() || createTable20Button.getSelection() || createTable50Button.getSelection() || createTableAllButton.getSelection() || createDumpButton.getSelection();
	}

	@Override
	public void performNext() {
		// TODO Auto-generated method stub
		
	}
}
