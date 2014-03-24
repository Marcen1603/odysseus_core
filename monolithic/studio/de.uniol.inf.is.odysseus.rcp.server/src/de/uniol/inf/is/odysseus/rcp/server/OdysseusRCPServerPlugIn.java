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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class OdysseusRCPServerPlugIn extends AbstractUIPlugin implements
		IUpdateEventListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(OdysseusRCPServerPlugIn.class);

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.server";

	public static final String USER_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.UserView";
	public static final String PARTIAL_PLAN_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.PartialPlanView";
	public static final String SOURCES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SourcesView";
	public static final String SINK_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SinkView";
	public static final String MEP_FUNCTIONS_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.MEPFunctionsView";
	public static final String STORED_PROCEDURES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.StoredProceduresView";

	private static IExecutor executor;

	private static ImageManager imageManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		imageManager = new ImageManager(context.getBundle());
		imageManager.register("source", "icons/sources.png");
		imageManager.register("sink", "icons/sinks.png");
		imageManager.register("attribute", "icons/status.png");
		imageManager.register("loggedinuser", "icons/user--plus.png");
		imageManager.register("user", "icons/user.png");
		imageManager.register("role", "icons/tick-small-circle.png");
		imageManager.register("function", "icons/function.png");
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
		OdysseusRCPServerPlugIn.executor = executor;
		OdysseusRCPServerPlugIn.executor.addUpdateEventListener(this, IUpdateEventListener.SCHEDULING, null);
		OdysseusRCPServerPlugIn.executor.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
		LOG.debug("Executor " + executor + " bound");
	}

	public void unbindExecutor(IExecutor executor) {
		if (executor == OdysseusRCPServerPlugIn.executor) {
			OdysseusRCPServerPlugIn.executor.removeUpdateEventListener(this, IUpdateEventListener.SCHEDULING, null);
			OdysseusRCPServerPlugIn.executor.removeUpdateEventListener(this, IUpdateEventListener.QUERY, null);
			OdysseusRCPServerPlugIn.executor = null;
			LOG.debug("Executor " + executor + " unbound.");
		}
	}

	public static IExecutor getExecutor() {
		return OdysseusRCPServerPlugIn.executor;
	}

	@Override
	public void eventOccured() {
		try {
			StatusBarManager.getInstance().setMessage(
					StatusBarManager.SCHEDULER_ID,
					determineStatusManagerExecutorInfo());
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}


	private String determineStatusManagerExecutorInfo() {
		return OdysseusRCPServerPlugIn.executor.getCurrentSchedulerID(OdysseusRCPPlugIn
				.getActiveSession())
				+ " ("
				+ executor
						.getCurrentSchedulingStrategyID(OdysseusRCPPlugIn
								.getActiveSession()) + ") "
		// + (executor.isRunning() ? "Running" : "Stopped")
		;
	}
}
