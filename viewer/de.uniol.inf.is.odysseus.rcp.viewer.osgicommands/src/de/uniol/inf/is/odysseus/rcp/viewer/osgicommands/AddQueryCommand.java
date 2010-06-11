package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.console.MySink;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class AddQueryCommand extends AbstractHandler implements IHandler {

	public static final String COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.AddQuery";
	public static final String QUERY_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.QueryParameter";
	public static final String PARSER_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.ParserParameter";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final String queryToExecute = event.getParameter(QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(PARSER_PARAMETER_ID);
		
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			if (executor != null) {
				// TODO: User einfuegen, der diese Query ausführt
				User user = new User("TODO.SetUser");
				executor.addQuery(queryToExecute, parserToUse, user, new ParameterDefaultRoot(new MySink()));
				
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
