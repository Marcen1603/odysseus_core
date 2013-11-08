package de.uniol.inf.is.odysseus.rcp.login;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionEPManager;
import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionRegistry;

public class RCPLoginPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.login"; 
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.LoginContribution";
	
	private static final LoginContributionRegistry registry = new LoginContributionRegistry();
	private static final LoginContributionEPManager epManager = new LoginContributionEPManager(registry);

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		epManager.checkForExtensionPoints();
		Platform.getExtensionRegistry().addListener(epManager, EXTENSION_POINT_ID);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		Platform.getExtensionRegistry().removeListener(epManager);
	}
	
	static LoginContributionRegistry getLoginContributionRegistry() {
		return registry;
	}
}