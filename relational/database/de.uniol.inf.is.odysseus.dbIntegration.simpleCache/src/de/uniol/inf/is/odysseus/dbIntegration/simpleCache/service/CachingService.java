package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;



public class CachingService implements ICache {

	private final String TIMEINCACHE = "timeInCache";
	private final String  MAXUSEDMB = "maxUsedMB";
	private ResourceBundle defaults;
	
	//key = DBQuery.hashCode
	private HashMap<Integer, DatabaseQuery> queries;
	
	//key = ParamKey.hashCode
	private HashMap<Integer, QueryParam > params;
	
	//key = ParamKey.hashCode
	private LinkedList<Integer> paramOrder;
	
	
	private long maxTime;
	private int maxObjects;
	private int currentObjects = 0;
	
	
	@Override
	public void addData(List<RelationalTuple<?>> result, DBQuery dbQuery) {
	}

	
	@Override
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs,
			IDataAccess dal) {
		long time = 0;
		try {
			if (queryPrefs != null) {
				for (String string : queryPrefs) {
					if (string.startsWith(TIMEINCACHE)) {
						string = string.replaceAll(TIMEINCACHE, "").trim();
						time = Long.valueOf(string);
					}
				}
			}
		} catch (NumberFormatException e) {
			
		}
		
		if (time <= 0) {
			time = maxTime;
		}
		queries.put(dbQuery.hashCode(), new DatabaseQuery(dbQuery, dal, time));
	}

	@Override
	public void closeQuery(DBQuery dbQuery) {
		DatabaseQuery query = queries.remove(dbQuery.hashCode());
		
		for(Integer param : query.getParams()) {
			params.remove(param);	
		}
		paramOrder.removeAll(query.getParams());		
	}

	@Override
	public List<RelationalTuple<?>> getCachedData(RelationalTuple<?> streamParam,
			DBQuery dbQuery) throws CacheMissException {
		DatabaseQuery query = queries.get(dbQuery.hashCode());
		
		if (query == null) {
			throw new CacheMissException();
		}
		LinkedList<RelationalTuple<?>> result;
		
		RelationalTuple<?> relevantStreamElement = query.getDataAccess().getRelevantParams(streamParam);
		
		
		Integer paramHash = (new ParamKey(relevantStreamElement, dbQuery)).hashCode();
		
		
		System.out.println(dbQuery.hashCode());

		if (query.getParams().contains(Integer.valueOf(paramHash))
				&& params.containsKey(paramHash)) {
			
			QueryParam param = params.get(paramHash);
			long currentTime = System.currentTimeMillis();
			
			if (param.getTimeInCache()+query.getMaxTimeInCache() > currentTime) {
				result = param.getResults();
				paramOrder.remove(paramHash);
				paramOrder.addFirst(paramHash);
				return result;
			} 
			currentObjects = currentObjects - query.getTupelSize() * param.getResults().size();
			paramOrder.remove(paramHash);
			params.remove(paramHash);
			query.removeParam(paramHash);
		} 
		result = (LinkedList<RelationalTuple<?>>) query.getDataAccess().executeBaseQuery(streamParam);
		
		if ((query.getTupelSize() * result.size()) > maxObjects) {
			throw new CacheMissException();
		}else{
			QueryParam newParam = new QueryParam(streamParam, result, System.currentTimeMillis(), dbQuery.hashCode());
			currentObjects = currentObjects + query.getTupelSize() * result.size();

			while (currentObjects > maxObjects) {
				System.out.println();
				Integer replaceHash = paramOrder.removeLast();
				if (replaceHash == null) {
					throw new CacheMissException();
				}
				QueryParam replaceParam = params.get(replaceHash);
				DatabaseQuery replaceQuery = queries.get(replaceParam.getQueryHash());
				this.currentObjects = currentObjects - (replaceQuery.getTupelSize() * replaceParam.getResults().size());
				replaceQuery.removeParam(replaceHash);				
			}

			params.put(paramHash, newParam);
			query.addParam(paramHash);
			paramOrder.addFirst(paramHash);
		}

		return result;	
	}

	protected void activate(ComponentContext componentContext) {
		defaults = ResourceBundle.getBundle("defaults");
		//TODO: moegliche MissingResourceException abfangen 
		
		maxTime = Long.parseLong(defaults.getString(TIMEINCACHE));
		
		maxObjects = Integer.parseInt(defaults.getString(MAXUSEDMB));
		maxObjects = 1024 * 1024 * maxObjects / 10;
//		maxObjects = 300;
		
		queries = new HashMap<Integer, DatabaseQuery>();
		params = new HashMap<Integer, QueryParam> ();
		paramOrder = new LinkedList<Integer>();
	}
	

}
