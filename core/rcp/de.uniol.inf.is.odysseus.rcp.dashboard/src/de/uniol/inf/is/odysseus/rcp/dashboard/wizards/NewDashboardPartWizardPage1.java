package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;

public class NewDashboardPartWizardPage1 extends WizardNewFileCreationPage {

	public NewDashboardPartWizardPage1(String pageName, IStructuredSelection selection ) {
		super(pageName, selection);
		
		setTitle("Choose project and folder");
		setDescription("Choose the project (and folder) in which the new dashboard part should be placed.");
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName("DashboardPart." + DashboardPlugIn.DASHBOARD_PART_EXTENSION);
	}
	
}
