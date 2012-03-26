package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;

/**
 * Command for dropping all registered sinks at once.
 * 
 * @author Timo Michelsen
 * 
 */
public class DropAllSinksCommand extends AbstractHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DropAllSinksCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LOG.debug("Dropping all sinks");

		final IExecutor executor = Preconditions.checkNotNull(OdysseusRCPPlugIn.getExecutor(), "Executor must not be null!");
		
		if (executor != null) {
			Job job = new Job("Drop all sinks") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						Set<Entry<String, ILogicalOperator>> sinks = executor.getSinks(OdysseusRCPPlugIn.getActiveSession());
						monitor.beginTask("Dropping sinks", sinks.size());
						
						ImmutableList<String> ids = determineIds(sinks);
						for( String id: ids) {
							executor.removeSink(id, OdysseusRCPPlugIn.getActiveSession());
							monitor.worked(1);
						}
						
						StatusBarManager.getInstance().setMessage("All Sinks dropped");
					} catch (PermissionException e) {
						return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cannot remove sink:\n See error log for details", e);
					}
					return Status.OK_STATUS;
				}

			};
			job.setUser(true);
			job.schedule();
		} 
		return null;
	}

	private static ImmutableList<String> determineIds(Set<Entry<String, ILogicalOperator>> sinks) {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for( Entry<String, ILogicalOperator> sink : sinks ) {
			builder.add(sink.getKey());
		}
		return builder.build();
	}

}
