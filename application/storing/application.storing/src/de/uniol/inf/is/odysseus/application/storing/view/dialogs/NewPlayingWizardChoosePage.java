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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Dennis Geesen Created at: 29.11.2011
 */
public class NewPlayingWizardChoosePage extends WizardPage  implements Listener {

	private Button createChartButton;
	private Button createTableButton;
	private Button createSinkButton;

	protected NewPlayingWizardChoosePage() {
		super("");
		setTitle("New Playback");
		setMessage("Please choose an optione where to stream the data to");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		// create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 4;
		gl.numColumns = ncol;
		composite.setLayout(gl);

		createTableButton = new Button(composite, SWT.RADIO);
		createTableButton.setText("Create a table");
		createTableButton.addListener(SWT.Selection, this);

		createChartButton = new Button(composite, SWT.RADIO);
		createChartButton.setText("Create a chart");
		createChartButton.addListener(SWT.Selection, this);

		createSinkButton = new Button(composite, SWT.RADIO);
		createSinkButton.setText("Choose another sink");
		createSinkButton.addListener(SWT.Selection, this);

		setControl(composite);
		setPageComplete(false);
	}

	@Override
	public IWizardPage getNextPage() {
		if (createTableButton.getSelection()) {
			return ((NewPlayingWizard) getWizard()).getCreateTablePage();
		}
		if (createChartButton.getSelection()) {
			return ((NewPlayingWizard) getWizard()).getCreateChartPage();
		}
		if (createSinkButton.getSelection()) {
			return ((NewPlayingWizard) getWizard()).getCreateSinkPage();
		}
		return super.getNextPage();
	}
	
	
	
	@Override
	public boolean canFlipToNextPage() {
		if(createChartButton.getSelection() || createSinkButton.getSelection() || createTableButton.getSelection()){
			return true;
		}
		return false;
	}

	@Override
	public void handleEvent(Event event) {
		getWizard().getContainer().updateButtons();		
	}

}
