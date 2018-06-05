/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.testcase.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.developer.testcase.editor.TestEditorPart;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestWizard extends Wizard implements INewWizard {
	private static final String TITLE_TEST = "New test case";
	private WizardNewFileCreationPage newFileCreationPage;
	private WizardTestPage testPage;
	private IStructuredSelection selection;

	/**
	 * Class constructor.
	 *
	 */
	public TestWizard() {
		super();
		setWindowTitle("Test Case Wizard");
	}

	@Override
	public void addPages() {
		super.addPages();
		this.createNewFileCreationPage();
		this.createTestPage();
		this.addPage(this.newFileCreationPage);
		this.addPage(this.testPage);
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		final IFile file = this.newFileCreationPage.createNewFile();
		if (file == null) {
			return false;
		}
		final TestModel model = TestModel.createEmpty(this.testPage.getOperator());
		model.setDirectory(this.testPage.getDirectory());
		model.setName(this.testPage.getTestcaseName());
		model.save(file);
		final FileEditorInput input = new FileEditorInput(file);
		try {
			final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			final IWorkbenchPage page = window.getActivePage();
			page.openEditor(input, TestEditorPart.ID);
		} catch (final PartInitException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void createNewFileCreationPage() {
		if (this.selection == null) {
			ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getSelectionService();
			String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
			this.selection = (IStructuredSelection) selectionService.getSelection(projExpID);
		}
		this.newFileCreationPage = new WizardNewFileCreationPage("Filename", this.selection);
		this.newFileCreationPage.setTitle(TestWizard.TITLE_TEST);
		this.newFileCreationPage.setDescription("Choose project and file name.");
		this.newFileCreationPage.setFileExtension("case");
		this.newFileCreationPage.setAllowExistingResources(false);
		this.newFileCreationPage.setFileName("test.case");
	}

	private void createTestPage() {
		this.testPage = new WizardTestPage("Operator");
	}
}
