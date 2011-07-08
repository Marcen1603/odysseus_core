/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.Collection;
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

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class AddQueryCommand extends AbstractHandler implements IHandler {

	private final Logger logger = LoggerFactory.getLogger(AddQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String queryToExecute = event.getParameter(OdysseusRCPPlugIn.QUERY_PARAMETER_ID);
		final String parserToUse = event.getParameter(OdysseusRCPPlugIn.PARSER_PARAMETER_ID);
		final String parameterTransformationConfigurationName = event.getParameter(OdysseusRCPPlugIn.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID);
		final User user = GlobalState.getActiveUser();
		final IDataDictionary dd = GlobalState.getActiveDatadictionary();
		
		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
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
						Collection<IQuery> queries = executor.addQuery(queryToExecute, parserToUse, user, dd, cfg.toArray(new IQueryBuildSetting[0]) );
						for (IQuery query:queries){
							executor.startQuery(query.getID(), user);
						}
						StatusBarManager.getInstance().setMessage("Query successfully added and started");
					} catch (Exception e) {
						e.printStackTrace();
						StatusBarManager.getInstance().setMessage("Adding query failed");
						return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant execute query:\n See error log for details", e );
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
