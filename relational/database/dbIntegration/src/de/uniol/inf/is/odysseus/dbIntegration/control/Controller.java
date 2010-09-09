package de.uniol.inf.is.odysseus.dbIntegration.control;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;



import de.uniol.inf.is.odysseus.dbIntegration.Activator;
import de.uniol.inf.is.odysseus.dbIntegration.dataAccess.DataAccess;
import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.exceptions.PrefetchException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IPrefetch;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * Der Controller ist das zentrale Element des Datenbankframeworks. Hier werden fuer den
 * entsprechenden Datenbankoperator ein Datenbankobjekt, Cache und Prefetching initialisiert
 * und gesteuert.
 * 
 * @author crolfes
 *
 */
public class Controller {

	//sollen Caching / Prefetching benutzt werden.
	private boolean caching = false;
	private boolean prefetching = false;
	
	//koennen Caching / Prefetching benutzt werden
	//falls Services nicht vorhanden -> false
	private boolean cacheAvailable;
	private boolean prefetchAvailable;
	
	private ICache cache;
	private IPrefetch prefetch;
	
	private DBQuery query;
	private List<String> cachePrefs = new LinkedList<String>();
	private List<String> prefetchPrefs = new LinkedList<String>();
	private List<String> dbOptions;
	
	private DataAccess dataAccesss;
	private SDFAttributeList inputSchema;
	
	final static String CACHE = "cache";
	final static String BOTH = "both";
	final static String NONE = "none";
	
	private CacheServiceTracker cacheServiceTracker;
	private PrefetchServiceTracker prefetchServiceTracker;
	
	
	
	/**
	 * StandardController
	 * 
	 * @param query - die Datenbankanfrage
	 */
	public Controller(DBQuery query) {
		this.query = query;
	}
	
	/**
	 * Controller fuer manipulierende Anfragen.
	 * 
	 * @param query - die Datenbankanfrage
	 * @param inputSchema - das anliegende Schema des untergeordneten Operators
	 */
	public Controller(DBQuery query, SDFAttributeList inputSchema) {
		this.query = query;
		this.inputSchema = inputSchema;
		init();
	}

	/**
	 * Controller fuer Select-Anfragen.
	 * 
	 * @param query - die Datenbankanfrage
	 * @param dbOptions - eine Liste mit moeglichen Optionen zur Steuerung des Controllers (Cache/Prefetching)
	 * und der Caching- bzw. Prefetchingkomponenten. Um Caching zu ermoeglichen muss 'ctrl cache' uebergeben werden.
	 * Soll weiterhin Prefetching moeglich sein, muss anstelle von 'ctrl cache' der String 'ctrl both' uebergeben
	 * werden. Sollen Optionen fuer eine Cachingkomponente gesetzt werden, Beginn des Strings mit 'ca'. Fuer Prefetching 
	 * 'pr'.
	 * @param inputSchema - das anliegende Schema des untergeordneten Operators.
	 */
	public Controller(DBQuery query, List<String> dbOptions, SDFAttributeList inputSchema) {
		this.query = query;
		this.dbOptions = dbOptions;
		this.inputSchema = inputSchema;
		init();
		
	}
	
	/**
	 * Die init-Methode initialisiert den Datenbankzugriff und setzt falls ueber
	 * die dbOptions vom Benutzer gewuenscht, die Tracker fuer Caching und Prefetching.
	 */
	private void init() {
		dataAccesss = new DataAccess(query, inputSchema);
		if (!query.isUpdate()) {
			for (String string : dbOptions) {
				if (string.startsWith("ctrl"))  {
					string = string.replaceFirst("ctrl", "").trim();
					if (string.equals(NONE)) {
						caching = false;
						prefetching = false;
					} else if (string.equals(CACHE)) {
						caching = true;
						prefetching = false;
					} else if (string.equals(BOTH)) {
						caching = true;
						prefetching = true;
					}
				} else if (string.startsWith("ca ")) {
					cachePrefs.add(string.replaceFirst("ca ", "").trim());
				} else if (string.startsWith("pr ")) {
					prefetchPrefs.add(string.replaceFirst("pr ", "").trim());
				}
			}
			if (caching) {
				cacheServiceTracker = new CacheServiceTracker(Activator.getContext());
				cacheServiceTracker.open();
				if (prefetching) {
					prefetchServiceTracker = new PrefetchServiceTracker(Activator.getContext());
					prefetchServiceTracker.open();
				}
			}
		}	
	}
	
	
	
	/**
	 * Diese Methode liefert dem aufrufenden Operator zu dem uebergebenen Datenstromtupel 
	 * eine Liste von Ergebnissen in Form von RelationalTuple-Objekten zurueck.
	 * 
	 * @param object - Das eingehende Datenstromtupel.
	 * @return die Liste mit Ergebnissen oder null, falls keine Ergebnisse geladen worden konnten.
	 */
	public List<RelationalTuple<?>> getData(Object object) {
		RelationalTuple<?> tuple = (RelationalTuple<?>) object; 
		List<RelationalTuple<?>> result = null;
		try {
			if (cacheAvailable) {
				if (prefetchAvailable) {
					prefetch.addDataStreamTuple(query, tuple);
				}
				result = cache.getCachedData(tuple, query);
				
			}
		} catch (CacheMissException cacheMiss) {
			if (prefetchAvailable) {
				try {
					result = prefetch.getData(tuple, query);
					if (result != null) {
						cache.addData(tuple, result, query);
					}
				} catch (PrefetchException pe) {
				} 
			}
		} finally {
			if (result == null) {
				DBResult results = dataAccesss.executeBaseQuery(tuple);
				
				if (results != null) {
					if (results.getResult() != null) {
						result = results.getResult();
						if (cacheAvailable) {
							cache.addData(tuple, results.getResult(), query);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * closeQuery schließt moegliche angeschlossene Queries in Cache oder Prefetching
	 * Komponenten.
	 */
	public void closeQuery() {
		if (cacheAvailable) {
			cache.closeQuery(query);
			if (prefetchAvailable) {
				prefetch.closeQuery(query);
			}
		}
		
		cacheServiceTracker.close();
		prefetchServiceTracker.close();
		
	}

	

	
	/**
	 * Liefert eine Liste von Strings. Diese Liste enthaelt die Attributsnamen
	 * des Ausgabeschemas.
	 * 
	 * @return das Ausgabeschema der Datenbankanfrage.
	 */
	public List<String> getOutAttributeSchema() {
		if (dataAccesss == null) {
			DataAccess tempDal = new DataAccess(query);
			return tempDal.getSqlOutputSchema(query);
		}
		return dataAccesss.getSqlOutputSchema(query);
	}
	
	
	
	
	/**
	 * Die Klasse CacheServiceTracker dient der Einbindung von CacheServices.
	 * Meldet sich ein Cache-Service an der OSGi-Service-Registry an, wird dieser 
	 * dem jeweiligen Controller uebergeben.
	 * 
	 * @author crolfes
	 *
	 */
	class CacheServiceTracker extends ServiceTracker {

		public CacheServiceTracker(BundleContext bundleContext) {
			super(bundleContext, ICache.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			
			ICache cacheService = (ICache) context
			    .getService(reference);
			
			if (cacheService != null && caching) {
				cache = cacheService;
				cache.addQuery(query, cachePrefs, dataAccesss);
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
	
	

	/**
	 * Die Klasse PrefetchServiceTracker dient der Einbindung von PrefetchServices.
	 * Meldet sich ein Prefetch-Service an der OSGi-Service-Registry an, wird dieser 
	 * dem jeweiligen Controller uebergeben.
	 * 
	 * @author crolfes
	 *
	 */
	class PrefetchServiceTracker extends ServiceTracker {

		public PrefetchServiceTracker(BundleContext bundleContext) {
			super(bundleContext, IPrefetch.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			IPrefetch prefetchService = (IPrefetch) context
			    .getService(reference);
			
			if (prefetchService != null && prefetching && cache != null) {
				prefetch = prefetchService;
				prefetch.addQuery(query, prefetchPrefs, dataAccesss, inputSchema, cache);
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
