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
package de.uniol.inf.is.odysseus.rcp.editor.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.OdysseusRCPEditorPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanExporter;

public class LogicalPlanWizard extends Wizard implements INewWizard {

	private CreateNewFileWizardPage page;

	public LogicalPlanWizard() {
		super();
		setWindowTitle("New Query Text");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new CreateNewFileWizardPage("CreateFilePage", selection, "Odysseus Script", "Query.pln");
	}

	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final IFile newFile = page.createNewFile();
		if (newFile != null) {
			// leeren plan reinschreiben
			OperatorPlan plan = new OperatorPlan();
			IOperatorPlanExporter exporter = new OperatorPlanExporter(newFile);
			exporter.save(plan);

			// open editor

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(newFile), OdysseusRCPEditorPlugIn.LOGICAL_PLAN_EDITOR_ID, true);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}});
			
		}
		return false;
	}
}
