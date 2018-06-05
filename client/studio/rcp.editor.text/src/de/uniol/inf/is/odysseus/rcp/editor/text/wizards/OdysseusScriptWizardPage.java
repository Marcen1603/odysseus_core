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
package de.uniol.inf.is.odysseus.rcp.editor.text.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class OdysseusScriptWizardPage extends WizardNewFileCreationPage {

	private static final String TITLE_TEXT = "New Odysseus Script";
	private static final String DESCRIPTION_TEXT = "Creates a new file for scripts";
	
	public OdysseusScriptWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		
		setTitle(TITLE_TEXT);
		setDescription(DESCRIPTION_TEXT);
	}
}
