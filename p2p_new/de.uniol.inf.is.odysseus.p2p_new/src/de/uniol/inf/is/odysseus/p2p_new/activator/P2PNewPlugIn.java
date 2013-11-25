package de.uniol.inf.is.odysseus.p2p_new.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class P2PNewPlugIn implements BundleActivator {

	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);

	private P2PActivatorThread activatorThread;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		activatorThread = new P2PActivatorThread(bundleContext);
		activatorThread.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}
