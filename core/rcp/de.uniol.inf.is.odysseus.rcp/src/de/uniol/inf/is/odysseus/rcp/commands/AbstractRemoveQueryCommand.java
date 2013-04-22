package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public abstract class AbstractRemoveQueryCommand extends AbstractHandler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(AbstractRemoveQueryCommand .class);

	
	public Object remove(Collection<Integer> queryIds) {
		
		for (final Integer qID: queryIds) {
			final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				Job job = new Job("Removing query") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							executor.removeQuery(qID, OdysseusRCPPlugIn.getActiveSession());
							StatusBarManager.getInstance().setMessage("Query removed successfully");
						} catch (PlanManagementException e) {
							return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant remove query:\n See error log for details", e);
						} catch (PermissionException e) {
							return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant remove query:\n See error log for details", e);
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
