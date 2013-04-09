package de.uniol.inf.is.odysseus.rcp.console;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//create console
		OdysseusConsole oc = new OdysseusConsole();
		oc.createConsole();
		// add it to log4j
		Logger.getRootLogger().addAppender(oc);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
