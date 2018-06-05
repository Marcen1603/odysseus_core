/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.extension.DashboardPartExtensionPointResolver;

public class DashboardPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPlugIn.class);

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.dashboard";
	public static final String DASHBOARD_PART_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartEditor";
	public static final String DASHBOARD_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.dashboard.DashboardEditor";
	
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.DashboardPart";
	public static final String DASHBOARD_PART_EXTENSION = "prt";
	public static final String DASHBOARD_EXTENSION = "dsh";
	
	public static final String DASHBOARD_PART_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.DashboardPartView";

	public static final String ADD_DASHBOARD_PART_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.AddDashboardPart";

    public static final String WIZARD_DASHBOARDPART_ID = "de.uniol.inf.is.odysseus.rcp.dashboard.wizards.NewDashboardPartWizard";
    public static final String WIZARD_DASHBOARD_ID = "de.uniol.inf.is.odysseus.rcp.dashboard.wizards.NewDashboardWizard";

    public static final String VIEW_DASHBOARDPARTVIEW_ID = "de.uniol.inf.is.odysseus.rcp.dashboard.views.dashboardpart.DashboardPartView";

	private static DashboardPartExtensionPointResolver extensionResolver;

	private static DashboardPlugIn plugin;	
	private static IExecutor executor;
	private static ImageManager imageManager;

	public void bindExecutor(IExecutor exec) {
		executor = exec;

		LOG.debug("Executor {} bound.", exec);
	}


	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		startImpl(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		stopImpl();
	}

	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;

			LOG.debug("Executor {} unbound.", exec);
		}
	}

	public static DashboardPlugIn getDefault() {
		return plugin;
	}

	public static IExecutor getExecutor() {
		return executor;
	}

	public static ImageManager getImageManager() {
		return imageManager;
	}

	private static void startImpl(BundleContext context) {
		extensionResolver = new DashboardPartExtensionPointResolver();
		Platform.getExtensionRegistry().addListener(extensionResolver, DashboardPlugIn.EXTENSION_POINT_ID);

		loadImages(context);
	}

	private static void loadImages(BundleContext context) {
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("dashboardPart", "icons/gear.png");
		imageManager.register("start", "icons/gear_run.png");
		imageManager.register("stop", "icons/gear_stop.png");
		imageManager.register("pause", "icons/gear_pause.png");
		imageManager.register("startAll", "icons/gears_run.png");
		imageManager.register("stopAll", "icons/gears_stop.png");
		imageManager.register("pauseAll", "icons/gears_pause.png");
		imageManager.register("removeAll", "icons/gears_delete.png");
		imageManager.register("resume", "icons/gear_replace.png");
		imageManager.register("layout", "icons/layout.gif");
		imageManager.register("selectImage", "icons/selectImage.gif");
		imageManager.register("resetImage", "icons/deleteEdit.gif");
		imageManager.register("setting", "icons/setting.png");
		imageManager.register("chooseFile", "icons/navigator.gif");
	}

	private static void stopImpl() {
		Platform.getExtensionRegistry().removeListener(extensionResolver);

		imageManager.disposeAll();
		imageManager = null;
	}
}
