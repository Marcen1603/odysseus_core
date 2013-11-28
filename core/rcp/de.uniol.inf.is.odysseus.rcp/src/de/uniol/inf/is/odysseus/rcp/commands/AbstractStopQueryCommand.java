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

public abstract class AbstractStopQueryCommand extends AbstractHandler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(StopQueryCommand.class);

	public Object stop(final Collection<Integer> collection){

			final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				
				final int queryCount = collection.size();
				Job job = new Job("Stopping " + queryCount + " queries") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Stopping " + queryCount + " queries", queryCount);
					int stopped = 0;
					for (final Integer qID : collection) {
						try {
							monitor.subTask("Stopping query " + (stopped + 1 ) + " of " + queryCount + ": QID=" + qID);
							executor.stopQuery(qID, OdysseusRCPPlugIn.getActiveSession());
							stopped++;
							monitor.worked(1);
							
							if( monitor.isCanceled() ) {
								break;
							}
							
						} catch (PlanManagementException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						} catch (PermissionException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cant stop query:\n See error log for details", e);
						}
					}
					StatusBarManager.getInstance().setMessage(stopped + " queries stopped");
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
