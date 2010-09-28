package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.IQueryViewConstants;

public class StopSchedulerCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IExecutor executor = Activator.getExecutor();
		if (executor != null) {
			try {
				executor.stopExecution();
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID, "Scheduler stopped");
				StatusBarManager.getInstance().setMessage("Scheduler stopped");

				// Queryview aktualisieren
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
				handlerService.executeCommand(IQueryViewConstants.REFRESH_COMMAND_ID, null);
			} catch (PlanManagementException e) {
				new ExceptionWindow(e);
				e.printStackTrace();
			} catch (NotDefinedException e) {
				e.printStackTrace();
			} catch (NotEnabledException e) {
				e.printStackTrace();
			} catch (NotHandledException e) {
				e.printStackTrace();
			}
		} else {
			StatusBarManager.getInstance().setMessage("No executor available");
			
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.OK);
		    box.setMessage("No executor available");
		    box.setText("Error");
		    box.open();

		}
		return null;
	}

}
