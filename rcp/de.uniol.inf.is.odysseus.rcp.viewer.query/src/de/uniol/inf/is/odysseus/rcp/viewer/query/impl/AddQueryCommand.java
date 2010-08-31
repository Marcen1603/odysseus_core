package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

public class AddQueryCommand extends AbstractHandler implements IHandler {

	private final Logger logger = LoggerFactory.getLogger(AddQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String queryToExecute = event.getParameter(IQueryConstants.QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(IQueryConstants.PARSER_PARAMETER_ID);
		final String parameterTransformationConfigurationName = event.getParameter(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID);
		final String userName = event.getParameter(IQueryConstants.USER_NAME_PARAMETER_ID);
		final String userPassword = event.getParameter(IQueryConstants.USER_PASSWORD_PARAMETER_ID);		
		final User user = UserManagement.getInstance().getUser(userName,userPassword);
		
		final IAdvancedExecutor executor = Activator.getExecutor();
		if (executor != null) {
			final ParameterTransformationConfiguration cfg = ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(parameterTransformationConfigurationName);
			if (cfg == null) {
				logger.error("ParameterTransformationConfiguration " + parameterTransformationConfigurationName + " nicht gefunden");
				return null;
			}

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						executor.addQuery(queryToExecute, parserToUse, user, new ParameterDefaultRoot(new MySink()), cfg);
						StatusBarManager.getInstance().setMessage("Query successfully added");
					} catch (PlanManagementException e) {
						new ExceptionWindow(e);
						e.printStackTrace();
						StatusBarManager.getInstance().setMessage("Adding query failed");
					}
				}

			});
			t.setDaemon(true);
			t.start();
			
		} else {
			logger.error("Kein ExecutorService gefunden");
			StatusBarManager.getInstance().setMessage("No executor ready for adding query");
			
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.OK);
		    box.setMessage("No executor available");
		    box.setText("Error");
		    box.open();

		}

		return null;
	}

}
