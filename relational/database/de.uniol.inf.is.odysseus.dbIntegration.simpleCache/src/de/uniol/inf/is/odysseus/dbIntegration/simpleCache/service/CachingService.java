package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


/**
 * Dieser Cache-Service implementiert das Interface ICache des Datenbankframeworks 
 * auf Basis des LRU-Algorithmus'. 
 * Ueber die default.properties-Datei koennen die Standardwerte fuer Verweildauer von 
 * Elementen im Cache und die ungefaehre Speichermenge angepasst werden.
 * 
 * @author crolfes
 *
 */
public class CachingService implements ICache {

	private final String TIMEINCACHE = "timeInCache";
	private final String  MAXUSEDMB = "maxUsedMB";
	private ResourceBundle defaults;
	
	//key = CacheQuery.hashCode
	private HashMap<Integer, CacheQuery> queries;
	
	//key = ResultKey.hashCode
	private HashMap<Integer, QueryResult > dbResults;
	
	//key = ParamKey.hashCode
	private LinkedList<Integer> resultOrder;
	
	
	private long maxTime = 100000; //Standard, falls properties-Fehler: 100sek;
	private long maxObjects = 1024 * 1024 * 100; //Standard, falls properties-fehler: 100 MB
	private long currentObjects = 0;
	
	private int hit = 0;
	protected Logger logger = LoggerFactory.getLogger(CachingService.class);
	
	
	
	@Override
	public void addData(RelationalTuple<?> streamParam, List<RelationalTuple<?>> result, DBQuery dbQuery) {
		if (result == null) 
			return;
		if (result.size() == 0) 
			return;
		
		CacheQuery query = queries.get(dbQuery.hashCode());
		
		QueryResult newResult = new QueryResult(streamParam, 
											(LinkedList<RelationalTuple<?>>) result, 
											System.currentTimeMillis(), 
											dbQuery.hashCode());
		
		if (query.getTupelSize() == -1) {
			query.setTupelSize(streamParam.memSize(true));
			query.setResultTupelSize(result.get(0).memSize(true));
		}

		currentObjects = currentObjects + query.getTupelSize() + result.size() * query.getResultTupleSize();
		
		RelationalTuple<?> relevantStreamElement = query.getDataAccess().getRelevantSQLObjects(streamParam, null);
		Integer resultHash = (new ResultKey(relevantStreamElement, dbQuery)).hashCode();
		
		if (dbResults.containsKey(resultHash)) {			
			QueryResult param = dbResults.get(resultHash);
			currentObjects = currentObjects - (query.getTupelSize() + query.getResultTupleSize() * param.getResults().size());
			resultOrder.remove(resultHash);
			dbResults.remove(resultHash);
			query.removeResultHash(resultHash);
		}

		while (currentObjects > maxObjects) {
			Integer replaceHash = resultOrder.removeLast();
			QueryResult replaceParam = dbResults.remove(replaceHash);
			CacheQuery replaceQuery = queries.get(replaceParam.getQueryHash());
			this.currentObjects = currentObjects - (replaceQuery.getTupelSize() + replaceParam.getResults().size() * replaceQuery.getResultTupleSize());
			replaceQuery.removeResultHash(replaceHash);		
		}

		dbResults.put(resultHash, newResult);
		query.addParam(resultHash);
		resultOrder.addFirst(resultHash);
	}

	
	
	@Override
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs,
			IDataAccess dal) {
		try {
			if (queryPrefs != null) {
				for (String string : queryPrefs) {
					if (string.startsWith(TIMEINCACHE)) {
						string = string.replaceAll(TIMEINCACHE, "").trim();
						long temp = Long.valueOf(string);
						if (temp >= 1) {
							maxTime = temp;
						}
					} else if (string.startsWith(MAXUSEDMB)) {
						string = string.replaceAll(MAXUSEDMB, "").trim();
						long temp = Long.valueOf(string);
						if (temp >= 1) {
							maxObjects = temp * 1024 * 1024;
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			
		}
		queries.put(dbQuery.hashCode(), new CacheQuery(dbQuery, dal, maxTime));
	}

	
	
	@Override
	public void closeQuery(DBQuery dbQuery) {
		CacheQuery query = queries.remove(dbQuery.hashCode());
		
		for(Integer param : query.getDBResults()) {
			dbResults.remove(param);	
		}
		resultOrder.removeAll(query.getDBResults());		
	}

	
	
	@Override
	public List<RelationalTuple<?>> getCachedData(RelationalTuple<?> streamParam,
			DBQuery dbQuery) throws CacheMissException {
		CacheQuery query = queries.get(dbQuery.hashCode());
		if (query == null) {
			throw new CacheMissException();
		}
		LinkedList<RelationalTuple<?>> result;
		RelationalTuple<?> relevantStreamElement = query.getDataAccess().getRelevantSQLObjects(streamParam, null);
		Integer resultHash = (new ResultKey(relevantStreamElement, dbQuery)).hashCode();
		if (query.getDBResults().contains(Integer.valueOf(resultHash))
				&& dbResults.containsKey(resultHash)) {
			
			QueryResult cResult = dbResults.get(resultHash);
			long currentTime = System.currentTimeMillis();
			
			if (cResult.getTimeInCache()+query.getMaxTimeInCache() > currentTime) {
				result = cResult.getResults();
				resultOrder.remove(resultHash);
				resultOrder.addFirst(resultHash);
				hit++;
				logger.debug("query: " + dbQuery.hashCode() + " | Cache Hits: " + hit);
				return result;
			} 
			currentObjects = currentObjects - (query.getTupelSize() + query.getResultTupleSize() * cResult.getResults().size());
			resultOrder.remove(resultHash);
			dbResults.remove(resultHash);
			query.removeResultHash(resultHash);
			logger.debug("query: " + dbQuery.hashCode() + " | Cache Hit, but too old");
			
		} 
		logger.debug("query: " + dbQuery.hashCode() + " | Cache Miss");
		throw new CacheMissException();
	}

	
	
	protected void activate(ComponentContext componentContext) {
		//bei Fehler in der defaults.properties
		try {
			defaults = ResourceBundle.getBundle("defaults");
			
			try {
				maxTime = Long.parseLong(defaults.getString(TIMEINCACHE));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				maxObjects = Long.parseLong(defaults.getString(MAXUSEDMB));
				maxObjects = 1024 * 1024 * maxObjects;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			
		}
		queries = new HashMap<Integer, CacheQuery>();
		dbResults = new HashMap<Integer, QueryResult> ();
		resultOrder = new LinkedList<Integer>();
	}


	
	@Override
	public boolean dataCached(DBQuery dbQuery, RelationalTuple<?> streamParam) {
		CacheQuery query = queries.get(dbQuery.hashCode());
		RelationalTuple<?> relevantStreamElement = query.getDataAccess().getRelevantSQLObjects(streamParam, null);
		Integer resultHash = (new ResultKey(relevantStreamElement, dbQuery)).hashCode();
		QueryResult result = dbResults.get(resultHash);
		if (result != null) {
			if (result.getTimeInCache()+query.getMaxTimeInCache() < System.currentTimeMillis()) {
				currentObjects = currentObjects - (query.getTupelSize() + query.getResultTupleSize() * result.getResults().size());
				resultOrder.remove(resultHash);
				dbResults.remove(resultHash);
				query.removeResultHash(resultHash);
				
				return false;
			}
			return true;
		}
		return false;
	}
	

}
