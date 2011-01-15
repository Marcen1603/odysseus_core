package windperformancercp.views.performance;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class QueryWizardDialog extends WizardDialog {
	
	private PerformanceWizardDialogPresenter presenter;

	public QueryWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		this.presenter = new PerformanceWizardDialogPresenter(this);
	}
	


	

}
