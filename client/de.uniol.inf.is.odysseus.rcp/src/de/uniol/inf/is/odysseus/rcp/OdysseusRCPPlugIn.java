/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class OdysseusRCPPlugIn extends AbstractUIPlugin implements IUpdateEventListener {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.base";

	public static final String QUERY_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.query.QueryView";

	public static final String QUERIES_PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.perspectives.QueriesPerspective";

	public static final String REFRESH_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RefreshSourcesViewCommand";
	public static final String COLLAPSE_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.CollapseSourcesViewCommand";
	public static final String EXPAND_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ExpandSourcesViewCommand";
	public static final String REMOVE_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RemoveQueryCommand";
	public static final String STOP_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.StopQueryCommand";
	public static final String START_QUERY_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ResumeQueryCommand";
	public static final String COPY_QUERYTEXT_COMMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.QueryViewCopyCommand";
	public static final String REFRESH_QUERY_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RefreshQueryViewCommand";

	public static final String QUERY_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.QueryParameter";
	public static final String PARSER_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.ParserParameter";
	public static final String QUERY_ID_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.QueryIDParameter";
	public static final String PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.TransformationConfigurationNameParameter";
	public static final String USER_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.UserNameParameter";
	public static final String USER_PASSWORD_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.UserPasswordParameter";

	public static final String BUILD_CONFIGURATION_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.buildConfiguration";

	public static final String ODYSSEUS_PROJECT_NATURE_ID = "de.uniol.inf.is.odysseus.rcp.base.OdysseusProjectNature";

	public static final String RCP_USER_TOKEN = "RCP";

	public static final String SOURCES_NAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.SourceNameParameter";

	public static final String WIZARD_PROJECT_ID = "de.uniol.inf.is.odysseus.rcp.wizards.OdysseusProjectWizard";

	public static final String USER_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.UserView";
	public static final String PARTIAL_PLAN_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.PartialPlanView";
	public static final String SOURCES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SourcesView";
	public static final String SINK_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SinkView";
	public static final String MEP_FUNCTIONS_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.MEPFunctionsView";
	public static final String STORED_PROCEDURES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.StoredProceduresView";

	private static OdysseusRCPPlugIn instance;
	private static IExecutor executor = null;
	private static ISession activeSession;
	private static ImageManager imageManager;

	public static OdysseusRCPPlugIn getDefault() {
		return instance;
	}

	public static void setActiveSession(ISession session) {
		OdysseusRCPPlugIn.activeSession = session;
	}

	public static ISession getActiveSession() {
		return activeSession;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		// Bilder registrieren

		imageManager = new ImageManager(bundleContext.getBundle());
		// TODO: Find another image for a broken signal
		imageManager.register("broken", "icons/status.png");
		imageManager.register("access", "icons/repository_rep.gif");
		imageManager.register("repository", "icons/repository_rep.gif");
		imageManager.register("user", "icons/user.png");
		imageManager.register("sla", "icons/document-block.png");
		imageManager.register("percentile", "icons/document-tag.png");
		imageManager.register("view", "icons/table.png");
		imageManager.register("constraint", "icons/document-attribute-c.png");
		imageManager.register("unit", "icons/document-attribute-u.png");
		imageManager.register("source", "icons/sources.png");
		imageManager.register("sink", "icons/sinks.png");
		imageManager.register("attribute", "icons/status.png");
		imageManager.register("loggedinuser", "icons/user--plus.png");
		// imageManager.register("user", "icons/user.png");
		imageManager.register("role", "icons/tick-small-circle.png");
		imageManager.register("function", "icons/function.png");

		// load default icon sets from bundle
		imageManager.registerImageSet("white", "operator-images/icons_white.xml");
		imageManager.registerImageSet("black", "operator-images/icons_black.xml");
		imageManager.registerImageSet("default", "operator-images/icons_default.xml");

		// load external icon sets from Odysseus home
		File iconSetDir = new File(OdysseusBaseConfiguration.getHomeDir() + "/icons");
		if (iconSetDir.isDirectory()) {
			FilenameFilter filter = new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return new File(dir, name).isFile() && name.endsWith(".xml");
				}
			};
			File[] files = iconSetDir.listFiles(filter);
			for (File file : files) {
				imageManager.registerExternalImageSet(file);
			}
		}

		instance = this;

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);

		instance = null;

		imageManager.disposeAll();
	}

	public static ImageManager getImageManager() {
		return imageManager;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		ImageDescriptor i = imageDescriptorFromPlugin(PLUGIN_ID, path);
		if (i == null) {
			throw new IllegalArgumentException("Image " + path + " not found");
		}
		return i;
	}

	public static IExecutor getExecutor() {
		return executor;
	}

	public void bindExecutor(IExecutor ex) throws PlanManagementException {
		synchronized (OdysseusRCPPlugIn.class) {
			executor = ex;
			String name = executor.getName();
			if (name == null) {
				name = "";
			}
			StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, name + " " + OdysseusNLS.Ready);

			executor.addUpdateEventListener(this, IUpdateEventListener.SCHEDULING, null);
			executor.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);

			OdysseusRCPPlugIn.class.notifyAll();
		}

	}

	public static void waitForExecutor() throws InterruptedException {
		synchronized (OdysseusRCPPlugIn.class) {
			while (executor == null) {
				OdysseusRCPPlugIn.class.wait(1000);
			}
		}
	}

	public void unbindExecutor(IExecutor ex) {
		if (executor == ex) {
			executor.removeUpdateEventListener(this, IUpdateEventListener.SCHEDULING, null);
			executor.removeUpdateEventListener(this, IUpdateEventListener.QUERY, null);
			executor = null;
		}
	}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.SCHEDULING) {
			try {
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,
						determineStatusManagerExecutorInfo());
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	private String determineStatusManagerExecutorInfo() {
		return executor.getCurrentSchedulerID(OdysseusRCPPlugIn.getActiveSession()) + " ("
				+ executor.getCurrentSchedulingStrategyID(OdysseusRCPPlugIn.getActiveSession()) + ") ";
	}

}
