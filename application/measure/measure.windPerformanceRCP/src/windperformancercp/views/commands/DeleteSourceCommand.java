package windperformancercp.views.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteSourceCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowNewSourceDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		//final Shell dialogShell = new Shell(parent);

		//TODO: delete source as parameter
		//TODO: evtl. anzeigen, welche PMs dann davon betroffen sind
		
		MessageDialog.openConfirm(parent,"Delete Confirmation" , "Do you really want to delete this source?\n" +
					"Warning: This could delete some of the power measurements also.");

		return null;
	}

}
