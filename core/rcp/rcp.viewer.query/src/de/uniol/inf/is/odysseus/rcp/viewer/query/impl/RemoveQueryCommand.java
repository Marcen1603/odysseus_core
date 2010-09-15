package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class RemoveQueryCommand extends AddQueryCommand implements IHandler {

	private Logger logger = LoggerFactory.getLogger(RemoveQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String queryID = event.getParameter(IQueryConstants.QUERY_ID_PARAMETER_ID);

		int qID = -1;
		try {
			qID = Integer.valueOf(queryID);
		} catch (NumberFormatException ex) {
		}
		if (qID == -1) {
			Object obj = Helper.getSelection(event);
			if (obj instanceof IQuery) {
				qID = ((IQuery) obj).getID();
			} else {
				logger.error("Cannot find queryID");
				return null;
			}
		}

		final IExecutor executor = Activator.getExecutor();
		final int qID2 = qID; // final machen :-)
		if (executor != null) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						executor.removeQuery(qID2);
						StatusBarManager.getInstance().setMessage("Query removed successfully");
					} catch (PlanManagementException e) {
						new ExceptionWindow(e);
						e.printStackTrace();
					}
				}
			});
			t.setDaemon(true);
			t.start();
		} else {
			logger.error("Kein ExecutorService gefunden");
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.OK);
		    box.setMessage("No executor available");
		    box.setText("Error");
		    box.open();

			return null;
		}

		return null;
	}

}
