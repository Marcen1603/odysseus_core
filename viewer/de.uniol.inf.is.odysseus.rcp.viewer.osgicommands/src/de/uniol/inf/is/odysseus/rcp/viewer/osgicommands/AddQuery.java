package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.console.MySink;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class AddQuery extends AbstractHandler implements IHandler {

	public static String queryToExecute = "";
	public static String parserToUse = "";
	
	public static final String COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.AddQuery";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			if (executor != null) {
//				String parser = parser(executor);
				executor.addQuery(queryToExecute, parserToUse, new ParameterDefaultRoot(new MySink()));
				
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
//
//	private String parser(IAdvancedExecutor executor) {
//		Iterator<String> parsers;
//		try {
//			parsers = executor.getSupportedQueryParser().iterator();
//		} catch (PlanManagementException e) {
//			return null;
//		}
//		
//		String parser = "";
//		if (parsers != null && parsers.hasNext()) {
//			parser = parsers.next();
//			return parser;
//		}
//		return null;
//	}
}
