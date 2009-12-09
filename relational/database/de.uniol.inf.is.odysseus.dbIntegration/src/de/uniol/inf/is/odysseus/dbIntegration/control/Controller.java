package de.uniol.inf.is.odysseus.dbIntegration.control;

import java.util.List;



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
	
	private void init() {
		dal = new DataAccess(query, inputSchema);
		if (!query.isUpdate()) {
			setPreferences();
			
			if (caching) {
				cache = Activator.getCacheService();
				cache.addQuery(query, queryPrefs, dal);
			} 
			if (prefetching) {
				prefetch = Activator.getPrefetchService();
				prefetch.addQuery(query, queryPrefs, dal);
			}
		}		
	}
	
	private void setPreferences() {
		
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
			} else if (string.startsWith("ca")) {
				cachePrefs.add(string.replaceFirst("ca", "").trim());
			} else if (string.startsWith("pr")) {
				prefetchPrefs.add(string.replaceFirst("pr", "").trim());
			}
		}
	}
	
	
	
	public List<RelationalTuple<?>> getData(Object object) {
		RelationalTuple<?> tuple = (RelationalTuple<?>) object; 
		
		if (caching && Activator.getCacheService() != null) {
			try {
				return cache.getCachedData(tuple, query);
			} catch (CacheMissException e) {
				if (prefetching && Activator.getPrefetchService() != null) {
					try {
						List<RelationalTuple<?>> result = prefetch.getCachedData(tuple, query);
						cache.addData(result, query);
						return result;
					} catch (PrefetchException e1) {
					} 
				}
			}
		}
		System.out.println("out: " + tuple);
		System.out.println("outhash: " + tuple.hashCode());
		return dal.executeBaseQuery(tuple);
	}
	
	
	
	public void closeQuery() {
		if (caching) {
			cache.closeQuery(query);
			if (prefetching) {
				prefetch.closeQuery(query);
			}
		}
		
	}

	

	
	
	public List<String> getOutAttributeSchema() {
		if (dal == null) {
			DataAccess tempDal = new DataAccess(query);
			return tempDal.getOutAttributeSchema();
		}
		return dal.getOutAttributeSchema();
	}
	
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
}
