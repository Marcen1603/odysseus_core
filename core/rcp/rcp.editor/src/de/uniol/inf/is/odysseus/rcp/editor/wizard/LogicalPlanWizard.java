package de.uniol.inf.is.odysseus.rcp.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.ILogicalPlanEditorConstants;
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
		IFile newFile = page.createNewFile();
		if (newFile != null) {
			// leeren plan reinschreiben
			OperatorPlan plan = new OperatorPlan();
			IOperatorPlanExporter exporter = new OperatorPlanExporter(newFile);
			exporter.save(plan);

			// open editor
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(newFile), ILogicalPlanEditorConstants.LOGICAL_PLAN_EDITOR_ID, true);
			} catch (PartInitException e) {
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}
}
