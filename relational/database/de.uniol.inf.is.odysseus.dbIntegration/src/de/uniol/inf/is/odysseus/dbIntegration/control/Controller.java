package de.uniol.inf.is.odysseus.dbIntegration.control;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;



import de.uniol.inf.is.odysseus.dbIntegration.Activator;
import de.uniol.inf.is.odysseus.dbIntegration.dataAccess.DataAccess;
import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.exceptions.PrefetchException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IPrefetch;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class Controller {

	private boolean caching = false;
	private boolean prefetching = false;
	
	private ICache cache;
	private IPrefetch prefetch;
	
	private DBQuery query;
	private List<String> cachePrefs;
	private List<String> prefetchPrefs;
	private List<String> queryPrefs;
	
	private DataAccess dal;
	private SDFAttributeList inputSchema;
	
	final static String CACHE = "cache";
	final static String BOTH = "both";
	final static String NONE = "none";
	
	private CacheServiceTracker cacheServiceTracker;
	private PrefetchServiceTracker prefetchServiceTracker;
	private boolean cacheAvailable;
	private boolean prefetchAvailable;
	
	
	
	public Controller(DBQuery query) {
		this.query = query;
	}
	public Controller(DBQuery query, SDFAttributeList inputSchema) {
		this.query = query;
		this.inputSchema = inputSchema;
		init();
		
	}

	public Controller(DBQuery query, List<String> queryPrefs, SDFAttributeList inputSchema) {
		this.query = query;
		this.queryPrefs = queryPrefs;
		this.inputSchema = inputSchema;
		init();
		
	}
	
	
	private void init() {
		
		
		
		dal = new DataAccess(query, inputSchema);
		if (!query.isUpdate()) {
			for (String string : queryPrefs) {
				if (string.startsWith("ctrl"))  {
					string = string.replaceFirst("ctrl", "").trim();
					if (string.equals(NONE)) {
						caching = false;
						prefetching = false;
					} else if (string.equals(CACHE)) {
						caching = true;
						prefetching = false;
					} if (string.equals(BOTH)) {
						caching = true;
						prefetching = true;
					} 
				} else if (string.startsWith("ca ")) {
					cachePrefs.add(string.replaceFirst("ca ", "").trim());
				} else if (string.startsWith("pr ")) {
					prefetchPrefs.add(string.replaceFirst("pr ", "").trim());
				}
			}
		}	
		cacheServiceTracker = new CacheServiceTracker(Activator.getContext());
		cacheServiceTracker.open();
		
		prefetchServiceTracker = new PrefetchServiceTracker(Activator.getContext());
		prefetchServiceTracker.open();
	}
	
	
	
	
	public List<RelationalTuple<?>> getData(Object object) {
		RelationalTuple<?> tuple = (RelationalTuple<?>) object; 
		
		if (cacheAvailable) {
			try {
				return cache.getCachedData(tuple, query);
			} catch (CacheMissException e) {
				if (prefetchAvailable) {
					try {
						List<RelationalTuple<?>> result = prefetch.getCachedData(tuple, query);
						cache.addData(result, query);
						return result;
					} catch (PrefetchException e1) {
					} 
				}
			}
		}
		return dal.executeBaseQuery(tuple);
	}
	
	
	public void closeQuery() {
		if (caching) {
			cache.closeQuery(query);
			if (prefetching) {
				prefetch.closeQuery(query);
			}
		}
		
		cacheServiceTracker.close();
		prefetchServiceTracker.close();
		
	}

	

	
	
	public List<String> getOutAttributeSchema() {
		if (dal == null) {
			DataAccess tempDal = new DataAccess(query);
			return tempDal.getOutAttributeSchema(query);
		}
		return dal.getOutAttributeSchema(query);
	}
	
	
	
	
	
	class CacheServiceTracker extends ServiceTracker {

		public CacheServiceTracker(BundleContext bundleContext) {
			super(bundleContext, ICache.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			
			ICache cacheService = (ICache) context
			    .getService(reference);
			
			if (cacheService != null && caching) {
				cache = cacheService;
				cache.addQuery(query, cachePrefs, dal);
				cacheAvailable = true;
				
			} else {
				cacheAvailable = false;
			}
			return cacheService;
		}

		public void removedService(ServiceReference reference, Object service) {
			cacheAvailable = false;
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
			
			if (prefetchService != null && prefetching) {
				prefetch.addQuery(query, prefetchPrefs, dal);
				prefetchAvailable = true;
				
			} else {
				prefetchAvailable = false;
			}
			return prefetchService;
		}

		public void removedService(ServiceReference reference, Object service) {
			prefetchAvailable = false;
			context.ungetService(reference);
		}
	}
}
