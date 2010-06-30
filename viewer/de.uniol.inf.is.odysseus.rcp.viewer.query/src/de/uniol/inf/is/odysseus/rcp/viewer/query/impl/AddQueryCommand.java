package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class AddQueryCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final String queryToExecute = event.getParameter(IQueryConstants.QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(IQueryConstants.PARSER_PARAMETER_ID);
		
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			if (executor != null) {
				// TODO: User einfuegen, der diese Query ausf�hrt
				User user = new User("TODO.SetUser");
				executor.addQuery(queryToExecute, parserToUse, user, new ParameterDefaultRoot(new MySink()), Activator.getTrafoConfigParam());
				
			} else {
				System.out.println("Kein ExecutorService gefunden");
				// TODO: Nachricht hier anzeigen
				return null;
			}
		} catch (PlanManagementException e ) {
			e.printStackTrace();
		}

		return null;
	}

}
