package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.ui.ide.IDE;

public class OdysseusRCPPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp";
	
	public static final String TENANT_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.TenantView";
	public static final String NAVIGATOR_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.OdysseusProjectNavigatorView";
	public static final String SOURCES_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.SourcesView";
	
	public static final String OBSERVER_PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.perspectives.ObserverPerspective";
	public static final String QUERIES_PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.perspectives.QueriesPerspective";
	
	public static final String REFRESH_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.RefreshSourcesViewCommand";
	public static final String COLLAPSE_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.CollapseSourcesViewCommand";
	public static final String EXPAND_SOURCES_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ExpandSourcesViewCommand";
	
	private static OdysseusRCPPlugIn instance;
	
	public static OdysseusRCPPlugIn getDefault() {
		return instance;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		
		// Bilder registrieren
		ImageManager.getInstance().register("repository", "icons/repository_rep.gif");
		ImageManager.getInstance().register("user", "icons/user.png");
		ImageManager.getInstance().register("loggedinuser", "icons/user--plus.png");
		ImageManager.getInstance().register("users", "icons/users.png");
		ImageManager.getInstance().register("sla", "icons/document-block.png");
		ImageManager.getInstance().register("percentile", "icons/document-tag.png");
		ImageManager.getInstance().register("tenant", "icons/user-business.png");
		ImageManager.getInstance().register("role", "icons/tick-small-circle.png");
		ImageManager.getInstance().register("source", "icons/application-import.png");
		ImageManager.getInstance().register("view", "icons/table.png");
		ImageManager.getInstance().register("attribute", "icons/status.png");
		
		IDE.registerAdapters();
		
		instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		
		instance = null;
	}

}
