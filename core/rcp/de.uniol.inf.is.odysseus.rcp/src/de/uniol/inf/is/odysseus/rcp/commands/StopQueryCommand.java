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
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.usermanagement.PermissionException;


public class StopQueryCommand extends AbstractHandler implements IHandler {

	private Logger logger = LoggerFactory.getLogger(StopQueryCommand.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		int qID;

		List<IQuery> selectedObj = SelectionProvider.getSelection(event);
		for (IQuery obj : selectedObj) {
			if (obj instanceof IQuery) {
				qID = ((IQuery) obj).getID();
			} else {
				logger.error("Cannot find queryID");
				return null;
			}

			final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			final int qID2 = qID; // final machen :-)
			if (executor != null) {
				Job job = new Job("Stopped query") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							executor.stopQuery(qID2, OdysseusRCPPlugIn.getActiveSession());
							StatusBarManager.getInstance().setMessage("Query stopped");
						} catch (PlanManagementException e) {
							return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						} catch (PermissionException e) {
							return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						}
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			} else {
				logger.error(OdysseusNLS.NoExecutorFound);
				MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
				box.setMessage(OdysseusNLS.NoExecutorFound);
				box.setText("Error");
				box.open();

				return null;
			}
		}

		return null;
	}

}
