package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;


import java.util.LinkedList;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;

public class DatabaseQuery extends DBQuery{

	//key = paramKey.hashCode
	private LinkedList<Integer> params;
	private IDataAccess dal;
	private int tupelSize;
	private long maxTimeInCache;
	private long lastUsed;
	
	
	public DatabaseQuery(DBQuery dbQuery, IDataAccess dal, long maxTimeInCache) {
		super(dbQuery.getDatabase(), dbQuery.getQuery(), dbQuery.isUpdate());
		this.dal = dal;
		params = new LinkedList<Integer>();
		tupelSize = dal.getOutAttributeSchema(dbQuery).size();
		this.maxTimeInCache = maxTimeInCache;
		lastUsed = System.currentTimeMillis();
		
	}
	
	public LinkedList<Integer> getParams() {
		return params;
	}
	
	public void addParam(Integer paramHash) {
		params.add(paramHash);
	}
	
	public void removeParam(Integer paramHash) {
		params.remove(paramHash);
	}
	
	public IDataAccess getDataAccess() {
		return dal;
	}
	
	public int getTupelSize() {
		return tupelSize;
	}
	
	public long getMaxTimeInCache() {
		return maxTimeInCache;
	}
	
	
//	
//	public List<RelationalTuple<?>> getData(RelationalTuple<?> param) {
//		param = dal.getRelevantParams(param);
//		if (queryParams.containsKey(param)) {
//			
//		}
//		return null;
//	}
	
}
