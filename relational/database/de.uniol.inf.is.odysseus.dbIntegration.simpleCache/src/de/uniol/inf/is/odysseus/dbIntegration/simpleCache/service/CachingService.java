package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;

import java.util.HashMap;
import java.util.HashSet;
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
	
	private ResourceBundle defaults;
	
	
	private HashMap<String, DatabaseQuery> queries = new HashMap<String, DatabaseQuery>();
	private HashMap<Integer, QueryParam> params = new HashMap<Integer, QueryParam>();
	
	private long maxTime;
	
	@Override
	public void addData(List<RelationalTuple<?>> result, DBQuery dbQuery) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs,
			IDataAccess dal) {
		queries.put(dbQuery.getQuery(), new DatabaseQuery(dbQuery, dal));
		maxTime = Long.parseLong(defaults.getString(TIMEINCACHE));
	}

	@Override
	public void closeQuery(DBQuery dbQuery) {
		for (Integer param : queries.get(dbQuery.getQuery()).getParams()) {
			params.remove(param);
		}
		queries.remove(dbQuery.getQuery());
		
	}

	@Override
	public List<RelationalTuple<?>> getCachedData(RelationalTuple<?> streamParam,
			DBQuery dbQuery) throws CacheMissException {
		
		if (queries.containsKey(dbQuery.getQuery())) {
			
			DatabaseQuery query = queries.get(dbQuery.getQuery());
			RelationalTuple<?> relevantStreamElement = query.getDataAccess().getRelevantParams(streamParam);
			
			QueryParam param = params.get(relevantStreamElement.hashCode());
			LinkedList<RelationalTuple<?>> result;
			if (param != null) {
				if (param.getMaxTime() < System.currentTimeMillis()+maxTime) {
					param.setTime(0);
					System.out.println("cached object loaded");
					result = param.getResults();
//					return param.getResults();
				} else {
					
					
					
					query.removeParam(param.hashCode());
					params.remove(param.hashCode());
					
					result = (LinkedList<RelationalTuple<?>>) query.getDataAccess().executeBaseQuery(streamParam);
					
					QueryParam newParam = new QueryParam(streamParam, result, System.currentTimeMillis()+maxTime);
					Integer key = relevantStreamElement.hashCode();
					query.addParam(key);
					params.put(key, newParam);
					
					System.out.println("in cache, but old - database load");
					
//					return result;
				}
			} else {
				result = (LinkedList<RelationalTuple<?>>) query.getDataAccess().executeBaseQuery(streamParam);
			
				QueryParam newParam = new QueryParam(streamParam, result, System.currentTimeMillis()+maxTime);
				Integer key = relevantStreamElement.hashCode();
				query.addParam(key);
				params.put(key, newParam);
				
				System.out.println("not in cache - database load");
				
//				return result;
			}
			System.out.print(result);
			return result;

		}

		throw new CacheMissException();
	}

	protected void activate(ComponentContext componentContext) {
		defaults = ResourceBundle.getBundle("defaults");
		//TODO: moegliche MissingResourceException abfangen 
	}
	

}
