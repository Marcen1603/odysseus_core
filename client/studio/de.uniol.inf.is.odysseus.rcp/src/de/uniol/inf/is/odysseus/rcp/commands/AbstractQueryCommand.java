package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
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
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

abstract public class AbstractQueryCommand extends AbstractHandler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(AbstractQueryCommand.class);

	IExecutor getExecutor(){
		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		if (executor != null) {
			return executor;
		}

		logger.error(OdysseusNLS.NoExecutorFound);
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		box.setMessage(OdysseusNLS.NoExecutorFound);
		box.setText("Error");
		box.open();

		return null;
		
	}
	
	protected List<Integer> getAllQueries() {
		return new ArrayList<Integer>(getExecutor().getLogicalQueryIds(OdysseusRCPPlugIn.getActiveSession()));
	}
	
	protected List<Integer> getSelecteddQueries(final ExecutionEvent event) {
		List<Integer> elems = SelectionProvider.getSelection(event);
		return elems;
	}
	
	protected List<QueryState> getQueryStates(List<Integer> selectedObj, List<ISession> sessions) {
		return getExecutor().getQueryStates(selectedObj, sessions);
	}
	
	

	public Object execute(final Collection<Integer> queryIds, final IQueryCommandAction action) {
		final String actionText = action.getActionText();
		final IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		if (executor != null) {

			final int queryCount = queryIds.size();

			Job job = new Job(actionText + " " + queryCount + " queries") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(actionText + " " + queryCount + " queries", queryCount);
					int executed = 0;
					for (final Integer qID : queryIds) {
						try {
							monitor.subTask(actionText + " query " + (executed + 1) + "of " + queryCount + ": QID=" + qID);
							action.execute(executor, qID);
							executed++;
							monitor.worked(1);

							if (monitor.isCanceled()) {
								break;
							}

						} catch (PlanManagementException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Error " + actionText + " query:\n See error log for details", e);
						} catch (PermissionException e) {
							return new Status(IStatus.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Error " + actionText + " query:\n See error log for details", e);
						}
					}
					StatusBarManager.getInstance().setMessage(actionText + " " + executed + " queries successfully");
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
