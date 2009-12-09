package de.uniol.inf.is.odysseus.dbIntegration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IPrefetch;

public class Activator implements BundleActivator {
	

	private static BundleContext context;

	private static CacheServiceTracker cacheServiceTracker;
	private static PrefetchServiceTracker prefetchServiceTracker;
	
	
	public void start(BundleContext context) throws Exception {
		Activator.setContext(context);
		
		cacheServiceTracker = new CacheServiceTracker(context);
		cacheServiceTracker.open();
		
		prefetchServiceTracker = new PrefetchServiceTracker(context);
		prefetchServiceTracker.open();
		
	}


	public void stop(BundleContext context) throws Exception {
		cacheServiceTracker.close();
		prefetchServiceTracker.close();
	}

	public static BundleContext getContext() {
		return context;
	}

	private static void setContext(BundleContext context) {
		Activator.context = context;
		
	}

	public static ICache getCacheService() {
		if (cacheServiceTracker != null) {
			return (ICache) Activator.cacheServiceTracker.getService();
		}
		return null;
	}
	
	public static IPrefetch getPrefetchService() {
		if (prefetchServiceTracker != null) {
			return (IPrefetch) Activator.prefetchServiceTracker.getService();
		}
		return null;
	}



	/**
	 * Ein ServiceTracker, der bei Veraenderungen des TranslationService die
	 * entsprechende Grussbotschaft ausgibt
	 */
	class CacheServiceTracker extends ServiceTracker {

		public CacheServiceTracker(BundleContext bundleContext) {
			super(bundleContext, ICache.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			
			ICache cacheService = (ICache) context
			    .getService(reference);
			
//			if (cacheService != null) {
////				Activator.setCacheService(cacheService);
//			}
			return cacheService;
		}

		public void removedService(ServiceReference reference, Object service) {
//			Activator.setCacheService(null);
			context.ungetService(reference);
		}
	}
	
	
	
	class PrefetchServiceTracker extends ServiceTracker {

		public PrefetchServiceTracker(BundleContext bundleContext) {
			super(bundleContext, IPrefetch.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			IPrefetch prefetchService = (IPrefetch) context
			    .getService(reference);
			
//			if (prefetchService != null) {
////				Activator.setPrefetchService(prefetchService);
//			}
			return prefetchService;
		}

		public void removedService(ServiceReference reference, Object service) {
//			Activator.setPrefetchService(null);
			context.ungetService(reference);
		}
	}
}