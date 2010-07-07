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
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class StopQueryCommand extends AbstractHandler implements IHandler {

	private Logger logger = LoggerFactory.getLogger(StopQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				final String queryID = event.getParameter(IQueryConstants.QUERY_ID_PARAMETER_ID);

				int qID = -1;
				try {
					qID = Integer.valueOf(queryID);
				} catch( NumberFormatException ex ){}
				if(qID == -1) {
					Object obj = Helper.getSelection(event);
					if( obj instanceof IQuery ) {
						qID = ((IQuery)obj).getID();
					} else {
						logger.error("Cannot find queryID");
						return;
					}
				}
				
				try {
					IAdvancedExecutor executor = Activator.getExecutor();
					if (executor != null) {
						executor.stopQuery(qID);
					} else {
						logger.error("Kein ExecutorService gefunden");
						// TODO: Nachricht hier anzeigen
						return;
					}
				} catch (PlanManagementException e ) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(true);
		t.start();

		return null;
	}

}
