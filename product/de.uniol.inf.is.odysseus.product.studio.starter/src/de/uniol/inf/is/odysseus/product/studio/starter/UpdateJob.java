/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.product.studio.starter;

/**
 * @author Dennis Geesen
 *
 */
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * A Basic Update Job.
 * 
 * @author mahieddine.ichir@free.fr
 */
public class UpdateJob extends Job {

	private IProvisioningAgent agent;
	private IStatus result;

	/**
	 * Base constructor.
	 * @param agent Provisioning agent
	 */
	public UpdateJob(IProvisioningAgent agent) {
		super("Checking for updates");
		this.agent = agent;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		result = P2Util.checkForUpdates(agent, arg0);
		if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			popUpInformation("Nothing to update!");
		} else {
			installUpdates();
		}
		return result;
	}

	/**
	 * Pop up for updates installation and, if accepted, install updates.
	 */
	private void installUpdates() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean install = MessageDialog.openQuestion(null, "Updates", "Updates found! install?");
				if (install) {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
					try {
						dialog.run(true, true, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor arg0) throws InvocationTargetException,
							InterruptedException {
								P2Util.installUpdates(agent, arg0);
								PlatformUI.getWorkbench().restart();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						result = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Update failed!", e);
						StatusManager.getManager().handle(result);
					}
				}
			}
		});
	}

	/**
	 * Show a message dialog.
	 * @param message
	 */
	private void popUpInformation(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, "Updates", message);
			}
		});
	}
}
