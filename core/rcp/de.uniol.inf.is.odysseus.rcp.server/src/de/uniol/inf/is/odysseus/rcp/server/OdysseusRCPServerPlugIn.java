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
package de.uniol.inf.is.odysseus.rcp.server;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulingEvent.SchedulingEventType;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class OdysseusRCPServerPlugIn extends AbstractUIPlugin implements IEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusRCPServerPlugIn.class);
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.server"; 
	
	public static final String USER_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.UserView";
	public static final String PARTIAL_PLAN_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.PartialPlanView";
	public static final String SOURCES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SourcesView";
	public static final String SINK_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SinkView";
	public static final String MEP_FUNCTIONS_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.MEPFunctionsView";
	
	private static IServerExecutor serverExecutor;
	
	private static ImageManager imageManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("source", "icons/application-import.png");
		imageManager.register("attribute", "icons/status.png");
		imageManager.register("loggedinuser", "icons/user--plus.png");
		imageManager.register("user", "icons/user.png");
		imageManager.register("role", "icons/tick-small-circle.png");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		imageManager.disposeAll();
	}
	
	public static ImageManager getImageManager() {
		return imageManager;
	}
	
	public void bindExecutor(IExecutor executor) {
		if( executor instanceof IServerExecutor ) {
			serverExecutor = (IServerExecutor) executor;
			LOG.debug("ServerExecutor " + executor + " bound");
			
			prepareServerExecutor();
		
		} else {
			LOG.error("Using Executor of Class " + executor.getClass().getName() + " instead of " + IServerExecutor.class.getName());
		}
	}


	public void unbindExecutor(IExecutor executor) {
		if( executor == serverExecutor ) {
			serverExecutor = null;
			LOG.debug("ServerExecutor " + executor + " unbound.");
		} 
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED) {
			((SchedulerManagerEvent) event).getValue().unSubscribeFromAll(this);
		} else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
			((SchedulerManagerEvent) event).getValue().subscribeToAll(this);
		}

		if (event.getEventType() == SchedulingEventType.SCHEDULING_STARTED || event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED || event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED || event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
			try {
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID, determineStatusManagerExecutorInfo());
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void prepareServerExecutor() {
		StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID, determineStatusManagerExecutorInfo());

		if (serverExecutor.getSchedulerManager() != null) {
			serverExecutor.getSchedulerManager().subscribeToAll(this);
			serverExecutor.getSchedulerManager().getActiveScheduler().subscribeToAll(this);
		}
		
		serverExecutor.startExecution();
	}

	private String determineStatusManagerExecutorInfo() {
		return serverExecutor.getCurrentSchedulerID() + " (" + serverExecutor.getCurrentSchedulingStrategyID() + ") " + (serverExecutor.isRunning() ? "Running" : "Stopped" );
	}
}
