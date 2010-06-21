package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;


public class RemoveQueryCommand extends AddQueryCommand implements IHandler {

	private Logger logger = LoggerFactory.getLogger(RemoveQueryCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final String queryID = event.getParameter(IQueryConstants.QUERY_ID_ID);

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
				return null;
			}
		}
		
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			if (executor != null) {
				executor.removeQuery(qID);
			} else {
				logger.error("Kein ExecutorService gefunden");
				// TODO: Nachricht hier anzeigen
				return null;
			}
		} catch (PlanManagementException e ) {
			e.printStackTrace();
		}

		return null;
	}
	
}
