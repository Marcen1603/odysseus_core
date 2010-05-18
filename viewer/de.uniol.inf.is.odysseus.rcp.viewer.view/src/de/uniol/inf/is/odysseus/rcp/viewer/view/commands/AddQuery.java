package de.uniol.inf.is.odysseus.rcp.viewer.view.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.console.MySink;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Activator;

public class AddQuery extends AbstractHandler implements IHandler {

	public static String queryToExecute = "";
	
	public static final String COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.commands.addQuery";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String queryString = queryToExecute;
			
		ServiceTracker execTracker = new ServiceTracker(Activator.getContext(), IAdvancedExecutor.class.getName(), null);
		execTracker.open();
		IAdvancedExecutor executor;
		try {
			executor = (IAdvancedExecutor) execTracker.waitForService(300);
			if (executor != null) {
				String parser = parser(executor);
				executor.addQuery(queryString, parser, new ParameterDefaultRoot(new MySink()));
				
			} else {
				System.out.println("Kein ExecutorService gefunden");
				// TODO: Nachricht hier anzeigen
				return null;
			}
			execTracker.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
			// TODO: Nachricht hier anzeigen lassen
		} catch (PlanManagementException e ) {
			e.printStackTrace();
		}

		return null;
	}

	private String parser(IAdvancedExecutor executor) {
		Iterator<String> parsers;
		try {
			parsers = executor.getSupportedQueryParser().iterator();
		} catch (PlanManagementException e) {
			return null;
		}
		
		String parser = "";
		if (parsers != null && parsers.hasNext()) {
			parser = parsers.next();
			return parser;
		}
		return null;
	}
}
