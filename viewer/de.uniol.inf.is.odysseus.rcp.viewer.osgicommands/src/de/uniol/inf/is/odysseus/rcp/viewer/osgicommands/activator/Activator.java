package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public class Activator implements BundleActivator {

	private static IAdvancedExecutor executor = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		Thread t = new Thread( new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(context, IAdvancedExecutor.class.getName(), null);
				execTracker.open();
				IAdvancedExecutor e;
				try {
					e = (IAdvancedExecutor) execTracker.waitForService(0);
					if (e != null) {
						executor = e;
					} else {
					}
					execTracker.close();
				} catch (InterruptedException ex) {} 			
			}
			
		});
		
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

	public static IAdvancedExecutor getExecutor() {
		return executor;
	}
}
