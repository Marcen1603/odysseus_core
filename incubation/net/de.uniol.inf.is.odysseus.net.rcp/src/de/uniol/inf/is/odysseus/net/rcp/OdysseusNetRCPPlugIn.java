package de.uniol.inf.is.odysseus.net.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;

public class OdysseusNetRCPPlugIn extends AbstractUIPlugin {

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNetStartupManager startupManager;

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}
	
	public static IOdysseusNodeManager getOdysseusNodeManager() {
		return nodeManager;
	}

	// called by OSGi-DS
	public static void bindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		startupManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		if (startupManager == serv) {
			startupManager = null;
		}
	}
	
	public static IOdysseusNetStartupManager getOdysseusNetStartupManager() {
		return startupManager;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
