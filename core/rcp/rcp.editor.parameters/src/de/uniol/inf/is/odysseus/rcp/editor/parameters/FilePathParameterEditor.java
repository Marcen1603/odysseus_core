/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;


public class FilePathParameterEditor extends StringParameterEditor {


	@Override
	protected Control createInputControl(Composite parent) {
		Composite comp = new Composite( parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		
		// Input-SWT holen
		final Text ctrl = (Text)super.createInputControl(comp);
		ctrl.setLayoutData( new GridData( GridData.FILL_HORIZONTAL));
		
		// Button zur Fileauswahl
		Button fileSelectAuswahl = new Button( comp, SWT.PUSH );
		fileSelectAuswahl.setText("Browse...");
		fileSelectAuswahl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
				String selectedFile = dlg.open();
				if( selectedFile != null ) {
//					setValue(selectedFile);
					ctrl.setText(selectedFile);
				}
			}
		});
		
		return comp;
	}
}
