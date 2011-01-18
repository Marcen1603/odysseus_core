package de.uniol.inf.is.odysseus.rcp.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.ILogicalPlanEditorConstants;
import de.uniol.inf.is.odysseus.rcp.editor.model.IOperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlanExporter;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class NewLogicalPlanWizard extends Wizard implements INewWizard {

	private CreateNewFileWizardPage page;
	
	public NewLogicalPlanWizard() {
		super();
		setWindowTitle("New Query Text");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new CreateNewFileWizardPage("CreateFilePage", selection, "Odysseus Script", "Query.qry");
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		try {
			IFile newFile = page.createNewFile();
			
			// leeren plan reinschreiben
			OperatorPlan plan = new OperatorPlan();
			IOperatorPlanExporter exporter = new OperatorPlanExporter(newFile);
			exporter.save(plan);

			// open editor
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
					new FileEditorInput(newFile), 
					ILogicalPlanEditorConstants.LOGICAL_PLAN_EDITOR_ID, 
					true);
			
			return true;
		} catch (Exception ex ) {
			new ExceptionWindow(ex);
			return false;
		}
	}
}
