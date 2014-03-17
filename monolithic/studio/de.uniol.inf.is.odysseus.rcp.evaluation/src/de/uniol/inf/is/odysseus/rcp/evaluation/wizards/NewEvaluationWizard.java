package de.uniol.inf.is.odysseus.rcp.evaluation.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.evaluation.editor.EvaluationEditorPart;

public class NewEvaluationWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;
	private WizardNewFileCreationPage newFileCreationPage;

	public NewEvaluationWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPages() {
		addPage(newFileCreationPage);
	}

	private void createEmptyFilePage() {
		newFileCreationPage = new WizardNewFileCreationPage("Filename", this.selection);
		newFileCreationPage.setTitle("Create new evaluation job");
		newFileCreationPage.setDescription("Choose project and file name.");
		newFileCreationPage.setFileExtension("eval");
		newFileCreationPage.setAllowExistingResources(false);
		newFileCreationPage.setFileName("evaluation.eval");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		createEmptyFilePage();

	}

	@Override
	public boolean performFinish() {
		IFile file = newFileCreationPage.createNewFile();
		if (file == null) {
			return false;
		}
		FileEditorInput input = new FileEditorInput(file);
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();			
			page.openEditor(input, EvaluationEditorPart.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
