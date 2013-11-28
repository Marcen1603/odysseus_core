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

public abstract class AbstractRestartQueryCommand extends AbstractHandler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(AbstractRestartQueryCommand.class);

	
	public Object restart(final Collection<Integer> queryIds) {

		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		if (executor != null) {
			
			final int queryCount = queryIds.size();
			Job job = new Job("Restarting " + queryCount + " queries") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Restarting " + queryCount + " queries", queryCount);
					int restarted = 0;
					for (final Integer qID : queryIds) {
						try {
							monitor.subTask("Restarting query " + (restarted + 1) + " of " + queryCount + ": QID=" + qID);
							executor.stopQuery(qID, OdysseusRCPPlugIn.getActiveSession());
							executor.startQuery(qID, OdysseusRCPPlugIn.getActiveSession());
							monitor.worked(1);
							restarted++;
							
							if( monitor.isCanceled() ) {
								break;
							}
							
						} catch (PlanManagementException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						} catch (PermissionException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						}
					}
					StatusBarManager.getInstance().setMessage(restarted + " queries restarted");
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
		return null;
	}

}
