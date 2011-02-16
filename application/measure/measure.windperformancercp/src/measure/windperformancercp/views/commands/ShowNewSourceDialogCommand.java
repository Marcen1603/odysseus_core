package measure.windperformancercp.views.commands;

import measure.windperformancercp.views.AbstractUIDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import measure.windperformancercp.views.sources.SourceDialog;

public class ShowNewSourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windperformancercp.ShowNewSourceDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final Shell dialogShell = new Shell(parent);
		
			AbstractUIDialog dialog = new SourceDialog(dialogShell);
			dialog.open();
			
		return null;
	}

}
