package de.uniol.inf.is.odysseus.dbIntegration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;

public class Activator implements BundleActivator {
	
	private ConnectionServiceTracker connectionServiceTracker;
	private static BundleContext context;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Activator.setContext(context);
		
		connectionServiceTracker = new ConnectionServiceTracker(context);
		connectionServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		connectionServiceTracker.close();
	}

	public static BundleContext getContext() {
		return context;
	}

	private static void setContext(BundleContext context) {
		Activator.context = context;
	}
	
	class ConnectionServiceTracker extends ServiceTracker {

		public ConnectionServiceTracker(BundleContext bundleContext) {
			super(bundleContext, IConnectionData.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			IConnectionData conectionService = (IConnectionData) context
			    .getService(reference);
			
//			greet(conectionService, "hello", context.getBundle().getSymbolicName());
			return conectionService;
		}

		public void removedService(ServiceReference reference, Object service) {
//			greet((IConnectionData) service, "goodbye", context.getBundle()
//			    .getSymbolicName());

			// Referenz freigeben
			context.ungetService(reference);
		}
	}
	
	

}