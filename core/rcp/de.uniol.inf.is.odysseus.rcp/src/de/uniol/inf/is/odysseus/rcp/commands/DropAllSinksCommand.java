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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
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
		
		if (executor != null ) {
			final Set<Entry<String, ILogicalOperator>> sinks = executor.getSinks(OdysseusRCPPlugIn.getActiveSession());
			if( sinks == null || sinks.isEmpty() ) {
				LOG.debug("Nothing to drop");
				StatusBarManager.getInstance().setMessage("No sinks to drop");
				return null;
			}
			
			if( !confirm() ) {
				LOG.debug("Dropping all sinks not confirmed");
				return null;
			}
			
			Job job = new Job("Drop all sinks") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Dropping sinks", sinks.size());
						
						ImmutableList<String> ids = determineIds(sinks);
						for( String id: ids) {
							LOG.debug("Dropping sink " + id);
							monitor.subTask("Dropping " + id);
							executor.removeSink(id, OdysseusRCPPlugIn.getActiveSession());
							monitor.worked(1);
						}
						
						StatusBarManager.getInstance().setMessage("All Sinks dropped");
						LOG.debug("All sinks dropped");
						
					} catch (PermissionException e) {
						LOG.error("Could not drop all sinks", e);
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
	
	private static boolean confirm() {
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		box.setMessage("Are you sure to drop ALL sinks?");
		box.setText("Drop all sinks");
		return box.open() == SWT.OK;
	}

	private static ImmutableList<String> determineIds(Set<Entry<String, ILogicalOperator>> sinks) {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for( Entry<String, ILogicalOperator> sink : sinks ) {
			builder.add(sink.getKey());
		}
		return builder.build();
	}

}
