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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.OutputWizard;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.11.2011
 */
public class CreateSinkPage extends AbstractWizardPage<OutputWizard> {

	private Button chooseSinkButton;
	private Button createSinkButton;

	public CreateSinkPage(String title) {	
		super(title);
		setTitle(title);
		setMessage("Please choose sink type");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 1;
		gl.numColumns = ncol;
		composite.setLayout(gl);

		createSinkButton = new Button(composite, SWT.RADIO);
		createSinkButton.setText("Create a new sink");
		
		chooseSinkButton = new Button(composite, SWT.RADIO);
		chooseSinkButton.setText("Choose existing sink");

		
		
		setControl(composite);
		setPageComplete(false);

	}
	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void performNext() {
		// TODO Auto-generated method stub
		
	}

}
