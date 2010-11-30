package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class AddQueryCommand extends AbstractHandler implements IHandler {

	private final Logger logger = LoggerFactory.getLogger(AddQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String queryToExecute = event.getParameter(IQueryConstants.QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(IQueryConstants.PARSER_PARAMETER_ID);
		final String parameterTransformationConfigurationName = event.getParameter(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID);
		final User user = ActiveUser.getActiveUser();
		
		final IExecutor executor = Activator.getExecutor();
		if (executor != null) {
			final List<IQueryBuildSetting<?>> cfg = executor.getQueryBuildConfiguration(parameterTransformationConfigurationName);
			if (cfg == null) {
				logger.error("ParameterTransformationConfiguration " + parameterTransformationConfigurationName + " nicht gefunden");
				return null;
			}

			// Asynchron zur GUI ausführen, damit
			// die GUI bei längeren Queries
			// nicht warten muss.
			Job job = new Job("Add single Query") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						executor.addQuery(queryToExecute, parserToUse, user, cfg.toArray(new IQueryBuildSetting[0]) );
						StatusBarManager.getInstance().setMessage("Query successfully added");
					} catch (Exception e) {
						e.printStackTrace();
						StatusBarManager.getInstance().setMessage("Adding query failed");
						return new Status(Status.ERROR, IQueryConstants.PLUGIN_ID, "Cant execute query:\n See error log for details", e );
					}
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
			
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
