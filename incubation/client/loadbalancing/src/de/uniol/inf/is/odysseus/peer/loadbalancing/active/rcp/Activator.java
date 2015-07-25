package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;

public class Activator extends AbstractUIPlugin implements BundleActivator {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp";

	private static BundleContext context;


	private static ILoadBalancingController controller;
	private static ILoadBalancingLock lock;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	
	public static void bindLoadBalancingController(ILoadBalancingController control) {
		controller = control;
	}
	
	public static void unbindLoadBalancingController(ILoadBalancingController control) {
		if(controller == control) {
			controller=null;
		}
	}
	
	public static void bindLoadBalancingLock(ILoadBalancingLock serv) {
		lock = serv;
	}
	
	public static void unbindLoadBalancingLock(ILoadBalancingLock serv) {
		if(lock==serv) {
			lock = null;
		}
	}
	
	public static ILoadBalancingController getLoadBalancingController() {
		return controller;
	}
	
	public static ILoadBalancingLock getLock() {
		return lock;
	}


	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	

}
