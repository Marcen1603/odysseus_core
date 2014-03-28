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
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;

public class NewEvaluationWizard extends Wizard implements INewWizard {

	private static final String TITLE_NEW_EVALUATION_JOB = "New Evaluation Job";
	private IStructuredSelection selection;
	private WizardNewFileCreationPage newFileCreationPage;
	private ChooseQueryFilePage chooseQueryFilePage;

	public NewEvaluationWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPages() {
		addPage(newFileCreationPage);
		addPage(chooseQueryFilePage);
	}

	private void createNewFileCreationPage() {
		newFileCreationPage = new WizardNewFileCreationPage("Filename", this.selection);
		newFileCreationPage.setTitle(TITLE_NEW_EVALUATION_JOB);
		newFileCreationPage.setDescription("Choose project and file name.");
		newFileCreationPage.setFileExtension("eval");
		newFileCreationPage.setAllowExistingResources(false);
		newFileCreationPage.setFileName("evaluation.eval");		
	}
	
	private void createNewLinkPage(){
		chooseQueryFilePage = new ChooseQueryFilePage("Script to run");		
		chooseQueryFilePage.setTitle(TITLE_NEW_EVALUATION_JOB+"2");		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		createNewFileCreationPage();
		createNewLinkPage();

	}

	@Override
	public boolean performFinish() {
		
		IFile file = newFileCreationPage.createNewFile();
		if (file == null) {
			return false;
		}
		EvaluationModel model = EvaluationModel.createEmpty(chooseQueryFilePage.getQueryFile());
		model.save(file);
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
