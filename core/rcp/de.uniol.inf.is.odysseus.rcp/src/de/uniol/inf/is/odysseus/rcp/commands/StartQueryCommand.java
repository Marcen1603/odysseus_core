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
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class StartQueryCommand extends AbstractHandler implements IHandler {

	private Logger logger = LoggerFactory.getLogger(StartQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		

		final String queryID = event.getParameter(OdysseusRCPPlugIn.QUERY_ID_PARAMETER_ID);

		int qID = -1;
		try {
			qID = Integer.valueOf(queryID);
		} catch (NumberFormatException ex) {
		}
		if (qID == -1) {
			Object obj = SelectionProvider.getSelection(event);
			if (obj instanceof IQuery) {
				qID = ((IQuery) obj).getID();
			} else {
				logger.error("Cannot find queryID");
				return null;
			}
		}

		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		final int qID2 = qID; // final machen :-)
		if (executor != null) {
			// Asynchron zur GUI ausführen, damit
			// die GUI bei längeren Queries
			// nicht warten muss.
			Job job = new Job("Starting query") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						executor.startQuery(qID2, GlobalState.getActiveUser());
						StatusBarManager.getInstance().setMessage("Query started");
					} catch (PlanManagementException e) {
						e.printStackTrace();
						return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant start query:\n See error log for details", e );
					} catch (HasNoPermissionException e){
						e.printStackTrace();
						return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant start query:\n See error log for details", e );
					}
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();

		} else {
			logger.error("Kein ExecutorService gefunden");
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.OK);
		    box.setMessage("No executor available");
		    box.setText("Error");
		    box.open();

			return null;
		}

		return null;
	}

}
