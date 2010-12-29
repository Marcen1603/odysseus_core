package windperformancercp.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowCopySourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowCopySourceDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//TODO: werte des values uebergeben, am besten als command parameter (source)
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		final Shell dialogShell = new Shell(parent);
		SourceDialog dialog = new SourceDialog(dialogShell);
		//TODO: generate
		if(dialog.open()== Window.OK){
			System.out.println("Copy Source Handler says: Dialog says Ok!");
		}

		return null;
	}

}
