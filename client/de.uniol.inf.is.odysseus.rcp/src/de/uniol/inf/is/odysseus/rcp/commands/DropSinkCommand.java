/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;


/**
 * 
 * @author Dennis Geesen Created at: 03.11.2011
 */
public class DropSinkCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Logger logger = LoggerFactory.getLogger(DropSinkCommand.class);
		List<SinkInformation> selections = SelectionProvider.getSelection(event);
		for (SinkInformation selection : selections) {
			final String param = selection.getName().getResourceName().toString();
			final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				Job job = new Job("Drop sink") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							executor.removeSink(param, OdysseusRCPPlugIn.getActiveSession());
							StatusBarManager.getInstance().setMessage("Sink removed");
						} catch (PermissionException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cannot remove sink:\n See error log for details", e);
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
