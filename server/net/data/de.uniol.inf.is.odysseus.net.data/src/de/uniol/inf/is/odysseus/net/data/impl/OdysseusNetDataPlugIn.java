package de.uniol.inf.is.odysseus.net.data.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;

public class OdysseusNetDataPlugIn implements BundleActivator {

	private static IOdysseusNodeManager nodeManager;
	
	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

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
	
	public static IOdysseusNodeManager getNodeManager() {
		return nodeManager;
	}
}
