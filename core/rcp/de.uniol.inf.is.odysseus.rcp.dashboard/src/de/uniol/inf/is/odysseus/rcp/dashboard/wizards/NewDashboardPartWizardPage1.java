package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewDashboardPartWizardPage1 extends WizardNewFileCreationPage {

	public NewDashboardPartWizardPage1(String pageName, IStructuredSelection selection ) {
		super(pageName, selection);
		
		setTitle("Create a new DashboardPart instance.");
		setDescription("Create a new DashboardPart instance.");
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName("DashboardPart.prt");
		setPageComplete(validatePage());
	}
	
}
