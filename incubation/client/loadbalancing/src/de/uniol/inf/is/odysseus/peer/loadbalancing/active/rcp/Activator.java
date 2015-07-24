package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;

public class Activator implements BundleActivator {

	private static BundleContext context;


	private static ILoadBalancingController controller;

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
	
	public static ILoadBalancingController getLoadBalancingController() {
		return controller;
	}
	
	

}
