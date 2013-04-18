/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class ContainerSelectionPage extends WizardNewFileCreationPage {

	private final String defaultFileName;

	public ContainerSelectionPage(String pageName, IStructuredSelection selection) {
		this(pageName, selection, null);
	}

	public ContainerSelectionPage(String pageName, IStructuredSelection selection, String defaultFileName) {
		super(pageName, selection);

		setTitle("Choose project and folder");
		setDescription("Choose the project (and folder) in which the new file should be placed.");

		this.defaultFileName = defaultFileName == null ? "" : defaultFileName;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName(defaultFileName);
	}

}
