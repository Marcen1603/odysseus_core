package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;


public class StopSchedulerCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IAdvancedExecutor executor = Activator.getExecutor();
		if( executor != null ) {
			try {
				executor.stopExecution();
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID, "Scheduler stopped");
			} catch (PlanManagementException e) {
				new ExceptionWindow(e);
				e.printStackTrace();
			}
		}
		return null;	}

}
