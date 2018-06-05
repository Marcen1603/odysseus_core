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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.wizard;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class CreateNewMapFileWizardPage extends WizardNewFileCreationPage {

	private String fileDescription;
	private String fileName;

	public CreateNewMapFileWizardPage(String pageName, IStructuredSelection selection, String fileDescription, String filename) {
		super(pageName, selection);
		setFileDescription(fileDescription);
		setFilename(filename);
		
		setTitle("Create a new " + getFileDescription());
		setDescription("Create a new " + getFileDescription());
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName(getFilename());
		setPageComplete(validatePage());
	}
	
	private void setFileDescription( String desc ) {
		Assert.isNotNull(desc);
		
		this.fileDescription = desc;
	}
	
	public final String getFileDescription() {
		return this.fileDescription;
	}
	
	private void setFilename( String fileName ) {
		Assert.isNotNull(fileName);
		
		this.fileName = fileName;
	}
	
	public final String getFilename() {
		return this.fileName;
	}
}
