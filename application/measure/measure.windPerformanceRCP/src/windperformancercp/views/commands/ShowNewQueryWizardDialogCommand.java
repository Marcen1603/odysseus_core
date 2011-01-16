package windperformancercp.views.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import windperformancercp.views.IPresenter;
import windperformancercp.views.performance.QueryWizard;
import windperformancercp.views.performance.QueryWizardDialog;

public class ShowNewQueryWizardDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowNewQueryWizardDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final Shell dialogShell = new Shell(parent);
		
		//TODO: evtl. von AbstractUIDialog ableiten/IUserIDIalog
			QueryWizardDialog dialog = new QueryWizardDialog(dialogShell, new QueryWizard()); 
			
			//IPresenter presenter = dialog.getPresenter();
			//dialog.create();
			dialog.open();
			
		return null;
	}

}
