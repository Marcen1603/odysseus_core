package de.uniol.inf.is.odysseus.peer.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.broadcast.impl.BroadcastListener;

public class Activator implements BundleActivator {

	private static BundleContext context;
	BroadcastListener listener;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		listener = new BroadcastListener();
		Thread t = new Thread(listener);
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		listener.stopListener();
		Activator.context = null;
	}

}
