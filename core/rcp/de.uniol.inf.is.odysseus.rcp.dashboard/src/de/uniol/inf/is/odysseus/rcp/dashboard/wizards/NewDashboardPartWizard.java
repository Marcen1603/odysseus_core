package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewDashboardPartWizard extends Wizard implements INewWizard {

	private NewDashboardPartWizardPage1 page1;
	
	public NewDashboardPartWizard() {
		super();
		setWindowTitle("New Odysseus Script");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page1 = new NewDashboardPartWizardPage1("New Dashboard Part", selection);
	}
	
	@Override
	public void addPages() {
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
