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
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;

public class DropAllSourcesCommand extends AbstractHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DropAllSourcesCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LOG.debug("Dropping all sources");

		final IExecutor executor = Preconditions.checkNotNull(OdysseusRCPPlugIn.getExecutor(), "Executor must not be null!");
		final ImmutableList<String> sources = determineSourceIds(executor.getStreamsAndViews(OdysseusRCPPlugIn.getActiveSession()));
				
		if( sources.isEmpty() ) {
			LOG.debug("Nothing to drop.");
			StatusBarManager.getInstance().setMessage("Nothing to drop.");
			return null;
		}
		
		if( !confirm() ) {
			LOG.debug("Dropping all sources not confirmed");
			return null;
		}
				
		Job job = new Job("Drop source") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Dropping sources", sources.size());
				ISession user = OdysseusRCPPlugIn.getActiveSession();
				try {
					for( String source : sources ) {
						LOG.debug("Dropping " + source);
						monitor.subTask("Dropping " + source);
						executor.addQuery("DROP STREAM " + source, "CQL", user, "Standard");
						monitor.worked(1);
					}
					StatusBarManager.getInstance().setMessage("All sources dropped");
					LOG.debug("All sources dropped");
					
				} catch (PlanManagementException e) {
					LOG.error("Could not drop all sources", e);
					return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cannot remove source:\n See error log for details", e);
				} catch (PermissionException e) {
					LOG.error("Could not drop all sources", e);
					return new Status(Status.ERROR, OdysseusRCPPlugIn.PLUGIN_ID, "Cannot remove source:\n See error log for details", e);
				}
				
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
		return null;
	}

	private static ImmutableList<String> determineSourceIds(Set<Entry<String, ILogicalOperator>> sources) {
		if( sources == null || sources.isEmpty() ) {
			return ImmutableList.of();
		}
		
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for( Entry<String, ILogicalOperator> source : sources ) {
			builder.add(source.getKey());
		}
		return builder.build();
	}

	private static boolean confirm() {
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		box.setMessage("Are you sure to drop ALL sources?");
		box.setText("Drop all sources");
		return box.open() == SWT.OK;
	}
}
