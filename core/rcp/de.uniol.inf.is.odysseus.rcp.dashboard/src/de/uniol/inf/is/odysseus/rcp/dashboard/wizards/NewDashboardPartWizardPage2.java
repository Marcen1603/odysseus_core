package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class NewDashboardPartWizardPage2 extends WizardPage {

	public NewDashboardPartWizardPage2(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite rootComposite = new Composite(parent, SWT.NONE);
		
		setControl(rootComposite);
		setPageComplete(true);
	}
	
}
