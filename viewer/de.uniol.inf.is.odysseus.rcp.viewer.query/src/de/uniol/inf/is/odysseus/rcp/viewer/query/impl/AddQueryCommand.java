package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

public class AddQueryCommand extends AbstractHandler implements IHandler {
	
	private final Logger logger = LoggerFactory.getLogger(AddQueryCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final String queryToExecute = event.getParameter(IQueryConstants.QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(IQueryConstants.PARSER_PARAMETER_ID);
		final String parameterTransformationConfigurationName = event.getParameter(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID);
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			if (executor != null) {
				// TODO: User einfuegen, der diese Query ausf�hrt
				User user = new User("TODO.SetUser");
				ParameterTransformationConfiguration cfg = ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(parameterTransformationConfigurationName);
				if( cfg == null ) {
					logger.error("ParameterTransformationConfiguration " + parameterTransformationConfigurationName + " nicht gefunden");
					return null;
				}
				executor.addQuery(queryToExecute, parserToUse, user, new ParameterDefaultRoot(new MySink()), cfg);
				
			} else {
				logger.error("Kein ExecutorService gefunden");
			}
		} catch (PlanManagementException e ) {
			logger.error("Konnte Command nicht ausführen: ", e);
		}

		return null;
	}

}
