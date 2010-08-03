package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class StartQueryCommand extends AbstractHandler implements IHandler {

	private Logger logger = LoggerFactory.getLogger(StartQueryCommand.class);

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

		final IAdvancedExecutor executor = Activator.getExecutor();
		final int qID2 = qID; // final machen :-)
		if (executor != null) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						executor.startQuery(qID2);
						StatusBarManager.getInstance().setMessage("Query started");
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
			return null;
		}

		return null;
	}

}
