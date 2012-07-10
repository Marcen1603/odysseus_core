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

package de.uniol.inf.is.odysseus.rcp.wizard.output.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.OutputWizard;

/**
 * 
 * @author Dennis Geesen
 * Created at: 01.12.2011
 */
public class ChooseOutputTypePage extends AbstractWizardPage<OutputWizard> {
	
	private Button createChartButton;
	private Button createTableButton;
	private Button createSinkButton;

	public ChooseOutputTypePage(String title) {
		super(title);
		setTitle(title);
		setMessage("Please choose the type of the output");
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
		createSinkButton.setText("Select another sink");
		createSinkButton.addListener(SWT.Selection, this);

		setControl(composite);
		setPageComplete(false);

	}
	
	@Override
	public IWizardPage getNextPage() {
		if (createTableButton.getSelection()) {
			return getWizard().getCreateTablePage();			
		}
		if (createChartButton.getSelection()) {
			return getWizard().getCreateChartPage();
		}
		if (createSinkButton.getSelection()) {
			return getWizard().getCreateSinkPage();
		}
		return super.getNextPage();
	}
	

	@Override
	public boolean canFinish() {	
		return false;
	}

	@Override
	public boolean canFlipToNextPage() {
		if(createChartButton.getSelection() || createSinkButton.getSelection() || createTableButton.getSelection()){
			return true;
		}
		return false;
	}

	@Override
	public void performNext() {
		
	}
}
