package de.uniol.inf.is.odysseus.rcp.login;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionEPManager;
import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionRegistry;

public class RCPLoginPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(RCPLoginPlugIn.class);
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.login"; 
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.LoginContribution";
	
	private static final LoginContributionRegistry registry = new LoginContributionRegistry();
	private static final LoginContributionEPManager epManager = new LoginContributionEPManager(registry);

	@Override
	public void start(BundleContext context) throws Exception {
		//System.out.println("WAKAAAAAAAAAAAAAAA");
		
		LOG.debug("Started login plugin");
		super.start(context);
		
		epManager.checkForExtensionPoints();
		Platform.getExtensionRegistry().addListener(epManager, EXTENSION_POINT_ID);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		LOG.debug("Stopped login plugin");
		
		super.stop(context);
		
		Platform.getExtensionRegistry().removeListener(epManager);
	}
	
	static LoginContributionRegistry getLoginContributionRegistry() {
		return registry;
	}
}