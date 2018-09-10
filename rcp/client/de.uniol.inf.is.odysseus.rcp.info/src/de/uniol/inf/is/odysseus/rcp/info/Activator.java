package de.uniol.inf.is.odysseus.rcp.info;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;

public class Activator extends AbstractUIPlugin {

	private static final RCPInfoServiceListener INFO_SERVICE_LISTENER = new RCPInfoServiceListener();

	private static Activator instance = null; 
			
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		InfoService.addInfoServiceListener(INFO_SERVICE_LISTENER);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		InfoService.removeInfoSeriveListener(INFO_SERVICE_LISTENER);
		
		instance = null;
		super.stop(context);
	}

	public static Activator getInstance() {
		return instance;
	}
	
	public static boolean isStarted() {
		return instance != null;
	}
}
